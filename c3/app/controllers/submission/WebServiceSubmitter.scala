package controllers.submission

import play.api.mvc.Results.Redirect
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import play.api.{http, Logger}
import com.google.inject.Inject
import play.api.cache.Cache
import play.api.Play.current
import services.TransactionIdService
import services.submission.FormSubmission
import ExecutionContext.Implicits.global
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.libs.ws.Response
import models.domain.Claim
import play.api.mvc.SimpleResult

class WebServiceSubmitter @Inject()(idService: TransactionIdService, claimSubmission: FormSubmission) extends Submitter {

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    val txnID = idService.generateId
    Logger.info(s"Retrieved Id : $txnID")

    claimSubmission.submitClaim(xml(claim, txnID)).map(
      response => {
        registerId(claim, txnID, SUBMITTED)
        processResponse(claim, txnID, response, request)
      }
    ).recover {
      case e: java.net.ConnectException => {
        Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
        errorAndCleanup(claim, txnID, COMMUNICATION_ERROR, request)
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError(SUBMIT) ! ${e.getMessage}")
        errorAndCleanup(claim, txnID, UNKNOWN_ERROR, request)
      }
    }
  }

  private def processResponse(claim: Claim, txnId: String, response: Response, request: Request[AnyContent]): PlainResult = {
    response.status match {
      case http.Status.OK =>
        val responseStr = response.body
        Logger.info(s"Received response : ${claim.key} : $responseStr")
        val responseXml = scala.xml.XML.loadString(responseStr)
        val result = (responseXml \\ "result").text
        Logger.info(s"Received result : ${claim.key} : $result")
        result match {
          case "response" => {
            updateStatus(claim, txnId, SUCCESS)
            respondWithSuccess(claim, txnId, request)
          }
          case "acknowledgement" => {
            updateStatus(claim, txnId, ACKNOWLEDGED)
            respondWithSuccess(claim, txnId, request)
          }
          case "error" => {
            val errorCode = (responseXml \\ "errorCode").text
            errorAndCleanup(claim, txnId, errorCode, request)
          }
          case _ => {
            Logger.error(s"Received error : $result, TxnId : $txnId, Headers : ${request.headers}")
            errorAndCleanup(claim, txnId, UNKNOWN_ERROR, request)
          }
        }
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndCleanup(claim, txnId, BAD_REQUEST_ERROR, request)
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndCleanup(claim, txnId, REQUEST_TIMEOUT_ERROR, request)
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndCleanup(claim, txnId, INTERNAL_SERVER_ERROR, request)
      case _ =>
        Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndCleanup(claim, txnId, UNKNOWN_ERROR, request)
    }
  }

  def respondWithSuccess(claim: Claim, txnId: String, request: Request[AnyContent]): SimpleResult[Results.EmptyContent] = {
    Logger.info(s"Successful submission : ${claim.key} : $txnId")
    clearCache(request, claim)
    Logger.debug(s"claim.key = ${claim.key}, $txnId")
    claim.key match {
      case CachedClaim.key => {
        Logger.debug(s"claim.key = ${claim.key}, $txnId")
        Redirect(controllers.routes.ThankYou.claim())
      }
      case CachedChangeOfCircs.key =>
        Redirect(controllers.routes.ThankYou.circs())
    }
  }

  private def errorAndCleanup(claim: Claim, txnId: String, code: String, request: Request[AnyContent]): PlainResult = {
    Logger.error(s"errorAndCleanup : ${claim.key} : $txnId : $code")
    clearCache(request, claim)
    updateStatus(claim, txnId, code)
    Redirect(controllers.routes.Application.error(claim.key))
  }

  def clearCache(request: Request[AnyContent], claim: Claim) {
    // Clear the cache to ensure no duplicate submission
    val key = request.session.get(claim.key).orNull
    Logger.debug(s"key = $key")
    Cache.set(key, None)
  }

  private[submission] def pollXml(correlationID: String, pollEndpoint: String) = {
    <poll>
      <correlationID>{correlationID}</correlationID>
      <pollEndpoint>{pollEndpoint}</pollEndpoint>
    </poll>
  }

  private def updateStatus(claim: Claim, id: String, statusCode: String) = {
    idService.updateStatus(id, statusCode, claimType(claim))
  }

  private def registerId(claim: Claim, id: String, statusCode: String) = {
    idService.registerId(id, statusCode, claimType(claim))
  }

  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val INTERNAL_SERVER_ERROR = "9004"
  val COMMUNICATION_ERROR = "9005"
}
