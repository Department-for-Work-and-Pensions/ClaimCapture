package controllers.s2_about_you

import controllers.mappings.Mappings

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.Logger
import scala.language.postfixOps
import controllers.mappings.NINOMappings._
import app.ConfigProperties._

object G1YourDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> carersNonEmptyText(maxLength = Mappings.five),
    "titleOther" -> optional(carersText(maxLength = Mappings.twenty)),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino,validNino),
    "dateOfBirth" -> dayMonthYear.verifying(validDate)
  )(YourDetails.apply)(YourDetails.unapply)
    .verifying("titleOther.required",YourDetails.verifyTitleOther _)
  )

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    Logger.debug(s"Start your details ${claim.key} ${claim.uuid}")
    track(YourDetails) { implicit claim => Ok(views.html.s2_about_you.g1_yourDetails(form.fill(YourDetails))(lang)) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("","titleOther.required",FormError("titleOther","constraint.required"))
        BadRequest(views.html.s2_about_you.g1_yourDetails(updatedFormWithErrors)(lang))
      },
      yourDetails => { // Show pay details if the person is below 62 years of age on the day of the claim (claim date)
          val payDetailsVisible = showPayDetails(claim, yourDetails)
          val updatedClaim = claim.showHideSection(payDetailsVisible, PayDetails)
          updatedClaim.update(yourDetails) -> Redirect(routes.G2MaritalStatus.present())
      })
  } withPreview()

  def showPayDetails(claim:Claim, yourDetails:YourDetails):Boolean = {
    claim.dateOfClaim match {
      case Some(dmy) => yourDetails.dateOfBirth.yearsDiffWith(dmy) < getProperty("age.hide.paydetails",60)
      case _ => false
    }
  }
}

