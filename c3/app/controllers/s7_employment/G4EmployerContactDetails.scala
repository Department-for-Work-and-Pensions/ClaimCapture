package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.EmployerContactDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G4EmployerContactDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> optional(text verifying validPhoneNumber)
  )(EmployerContactDetails.apply)(EmployerContactDetails.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(EmployerContactDetails) { implicit claim => Ok(views.html.s7_employment.g4_employerContactDetails(form.fillWithJobID(EmployerContactDetails, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g4_employerContactDetails(formWithErrors)),
      employerContactDetails => claim.update(jobs.update(employerContactDetails)) -> Redirect(routes.G5LastWage.present(jobID)))
  }
}