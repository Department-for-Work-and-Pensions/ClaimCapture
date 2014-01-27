package controllers.submission

import app.ConfigProperties._
import scala.concurrent.{ExecutionContext, Future}
import services.UnavailableTransactionIdException
import play.api.Logger
import models.domain.{Claimable, Claim}
import jmx.inspectors.{FastSubmissionNotifier, SubmissionNotifier}
import ExecutionContext.Implicits.global
import play.api.mvc.{Call, AnyContent, Request, Controller}


abstract class SubmissionController(submitter: Submitter) extends Controller with SubmissionNotifier with FastSubmissionNotifier {

  def checkTimeToCompleteAllSections(claimOrCircs: Claim with Claimable, currentTime: Long): Boolean

  def honeyPot(claim: Claim): Boolean

  def processSubmit(claimOrCircs: Claim, request: Request[AnyContent], errorPage: Call) = {

    if (isHoneyPotBot(claimOrCircs)) {
      // Only log honeypot for now.
      // May send to an error page in the future
      Logger.warn(s"Honeypot ! Headers : ${request.headers}")
    }

    if (isSpeedBot(claimOrCircs)) {
      // Only log speed check for now.
      // May send to an error page in the future
      Logger.warn(s"Speed check ! Headers : ${request.headers}")
    }

    try {
      fireNotification(claimOrCircs) {
        submitter.submit(claimOrCircs, request)
      }
    } catch {
      case e: UnavailableTransactionIdException =>
        Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
        Future(Redirect(errorPage))
      case e: java.lang.Exception =>
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
        Future(Redirect(errorPage))
    }
  }

  def isSpeedBot(claimOrCircs: Claim): Boolean = {
    val checkForBotSpeed = getProperty("checkForBotSpeed", default = false)
    checkForBotSpeed && checkTimeToCompleteAllSections(claimOrCircs, System.currentTimeMillis())
  }

  def isHoneyPotBot(claimOrCircs: Claim): Boolean = {
    val checkForBotHoneyPot = getProperty("checkForBotHoneyPot", default = false)
    checkForBotHoneyPot && honeyPot(claimOrCircs)
  }

  def evaluateTimeToCompleteAllSections(claim: Claim with Claimable, currentTime: Long = System.currentTimeMillis(), sectionExpectedTimes: Map[String, Long]) = {

    val expectedMinTimeToCompleteAllSections: Long = claim.sections.map(s => {
      sectionExpectedTimes.get(s.identifier.id) match {
        case Some(n) =>
          if (s.questionGroups.size > 0) n else 0
        case _ => 0
      }
    }).reduce(_ + _) // Aggregate all of the sectionExpectedTimes for completed sections only.

    val actualTimeToCompleteAllSections: Long = currentTime - claim.created

    val result = actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections

    if (result) {
      fireFastNotification(claim)
      Logger.error(s"Detected bot completing sections too quickly! actualTimeToCompleteAllSections: $actualTimeToCompleteAllSections < expectedMinTimeToCompleteAllSections: $expectedMinTimeToCompleteAllSections")
    }
    result
  }

}