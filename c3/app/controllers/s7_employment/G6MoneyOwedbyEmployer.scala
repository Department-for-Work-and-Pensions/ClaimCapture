package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AdditionalWageDetails, MoneyOwedbyEmployer}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G6MoneyOwedbyEmployer extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "howMuchOwed" -> optional(text),
    "owedPeriod" -> optional(periodFromTo),
    "owedFor" -> optional(text),
    "shouldBeenPaidBy" -> optional(dayMonthYear),
    "whenWillGetIt" -> optional(text)
  )(MoneyOwedbyEmployer.apply)(MoneyOwedbyEmployer.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AdditionalWageDetails) match {
      case Some(a: AdditionalWageDetails) if a.employerOwesYouMoney == `yes` =>
        track(MoneyOwedbyEmployer) { implicit claim => Ok(views.html.s7_employment.g6_moneyOwedByEmployer(form.fillWithJobID(MoneyOwedbyEmployer, jobID))) }
      case _ =>
        claim.update(jobs.delete(jobID, MoneyOwedbyEmployer)) -> Redirect(routes.G7PensionSchemes.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g6_moneyOwedByEmployer(formWithErrors)),
      moneyowed => claim.update(jobs.update(moneyowed)) -> Redirect(routes.G7PensionSchemes.present(jobID)))
  }
}