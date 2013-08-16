package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain._
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s7_employment.Employment._
import scala.Some

object G10ChildcareExpenses extends Controller with CachedClaim {
  def form(implicit claim: Claim) = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "howMuchCostChildcare" -> optional(text),
      "whoLooksAfterChildren" -> nonEmptyText,
      "relationToYou" -> nonEmptyText,
      "relationToPartner" -> optional(nonEmptyText),
      "relationToPersonYouCare" -> nonEmptyText
    )(ChildcareExpenses.apply)(ChildcareExpenses.unapply)
      .verifying("relationToPartner.required", validateRelationToPartner(claim, _)))

  def validateRelationToPartner(implicit claim: Claim, childcareExpenses: ChildcareExpenses) = {
    claim.questionGroup(MoreAboutYou) -> claim.questionGroup(PersonYouCareFor) match {
      case (Some(m: MoreAboutYou), Some(p: PersonYouCareFor)) if m.hadPartnerSinceClaimDate == "yes" && p.isPartnerPersonYouCareFor == "no" => childcareExpenses.relationToPartner.isDefined
      case _ => true
    }
  }

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterChildren == `yes`=>
        dispatch(Ok(views.html.s7_employment.g10_childcareExpenses(form.fillWithJobID(ChildcareExpenses, jobID), completedQuestionGroups(ChildcareExpenses, jobID))))
      case _ =>
        claim.update(jobs.delete(jobID, ChildcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "relationToPartner.required", FormError("relationToPartner", "error.required"))
        dispatch(BadRequest(views.html.s7_employment.g10_childcareExpenses(formWithErrorsUpdate, completedQuestionGroups(ChildcareExpenses, jobID))))
      },
      childcareExpenses => claim.update(jobs.update(childcareExpenses)) -> Redirect(routes.G11ChildcareProvider.present(jobID)))
  }
}