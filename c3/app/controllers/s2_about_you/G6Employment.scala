package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings._

object G6Employment extends Controller with AboutYouRouting with CachedClaim {
  val form = Form(
    mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
    )(Employment.apply)(Employment.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(ClaimDate) match {
      case Some(n) => Ok(views.html.s2_about_you.g6_employment(form.fill(Employment), completedQuestionGroups(Employment)))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g6_employment(formWithErrors, completedQuestionGroups(Employment))),
      employment => {
        val updatedClaim = claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, Employed)
                                .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, SelfEmployment)

        updatedClaim.update(employment) -> Redirect(routes.AboutYou.completed())
      })
  }
}