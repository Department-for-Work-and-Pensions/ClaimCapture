package controllers.submission

import play.api.mvc.Results.Redirect
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import play.api.{http, Logger}
import com.google.inject.Inject
import play.api.cache.Cache
import play.api.libs.ws.Response
import play.api.Play.current
import services.TransactionIdService
import services.submission.ClaimSubmission
import models.domain.Claim
import xml.DWPCAClaim
import models.view.CachedClaim
import ExecutionContext.Implicits.global

class WebServiceSubmitter @Inject()(idService: TransactionIdService, claimSubmission : ClaimSubmission) extends Submitter {

  def submit(claim: Claim, request : Request[AnyContent]): Future[PlainResult] = {
    retrieveRetryData(request) match {
      case Some(retryData) => {
        claimSubmission.retryClaim(pollXml(retryData.corrId, retryData.pollUrl)).map(
          response => {
            processResponse(retryData.txnId, response, request)
          }
        ).recover {
          case e: java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            Redirect("/consent-and-declaration/error")
          }
          case e: java.lang.Exception => {
            Logger.error(s"InternalServerError(RETRY) ! ${e.getMessage}")
            errorAndCleanup(retryData.txnId, UNKNOWN_ERROR)
          }
        }
      }
      case None => {
        val txnId = idService.generateId
        Logger.info(s"Retrieved Id : $txnId")
        val claimXml = DWPCAClaim.xml(claim, txnId)

        claimSubmission.submitClaim(claimXml).map(
          response => {
            idService.registerId(txnId, SUBMITTED)
            processResponse(txnId, response, request)
          }
        ).recover {
          case e: java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            Redirect("/consent-and-declaration/error")
          }
          case e: java.lang.Exception => {
            Logger.error(s"InternalServerError(SUBMIT) ! ${e.getMessage}")
            errorAndCleanup(txnId, UNKNOWN_ERROR)
          }
        }
      }
    }
  }

  def processResponse(txnId: String, response: Response, request: Request[AnyContent]): PlainResult = {
    response.status match {
      case http.Status.OK =>
        val responseStr = response.body
        Logger.info(s"Received response : $responseStr")
        val responseXml = scala.xml.XML.loadString(responseStr)
        val result = (responseXml \\ "result").text
        Logger.info(s"Received result : $result")
        result match {
          case "response" => {
            idService.updateStatus(txnId, SUCCESS)
            Redirect(s"/thankyou/$txnId").withNewSession
          }
          case "acknowledgement" => {
            idService.updateStatus(txnId, ACKNOWLEDGED)
            val correlationID = (responseXml \\ "correlationID").text
            val pollEndpoint = (responseXml \\ "pollEndpoint").text
            storeRetryData(RetryData(correlationID, pollEndpoint, txnId), request)
            Redirect("/consent-and-declaration/error")
          }
          case "error" => {
            val errorCode = (responseXml \\ "errorCode").text
            idService.updateStatus(txnId, errorCode)
            Logger.error(s"Received error : $result")
            Redirect("/consent-and-declaration/error")
          }
          case _ => {
            Logger.info(s"Received result : $result")
            errorAndCleanup(txnId, UNKNOWN_ERROR)
          }
        }
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
        errorAndCleanup(txnId, BAD_REQUEST_ERROR)
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
        errorAndCleanup(txnId, REQUEST_TIMEOUT_ERROR)
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
        errorAndCleanup(txnId, INTERNAL_SERVER_ERROR)
      case _ =>
        Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
        errorAndCleanup(txnId, UNKNOWN_ERROR)
    }
  }

  def errorAndCleanup(txnId: String, code: String): PlainResult = {
    idService.updateStatus(txnId, code)
    Redirect("/error").withNewSession
  }

  case class RetryData(corrId: String, pollUrl: String, txnId: String)

  def storeRetryData(data:RetryData, request : Request[AnyContent]) {
    val uuid = request.session.get(CachedClaim.claimKey)
    Cache.set(uuid+"_retry", data, 3000)
  }

  def retrieveRetryData(request : Request[AnyContent]) : Option[RetryData] = {
    val uuid = request.session.get(CachedClaim.claimKey)
    Cache.getAs[RetryData](uuid+"_retry")
  }

  def pollXml(correlationID: String, pollEndpoint: String) = {
    <poll>
      <correlationID>
        {correlationID}
      </correlationID>
      <pollEndpoint>
        {pollEndpoint}
      </pollEndpoint>
    </poll>
  }

  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val INTERNAL_SERVER_ERROR = "9004"
}