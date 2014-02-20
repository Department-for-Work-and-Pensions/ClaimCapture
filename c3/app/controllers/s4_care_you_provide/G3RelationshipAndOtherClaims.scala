package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.MoreAboutThePerson
import utils.helpers.CarersForm._
import controllers.Mappings._

object G3RelationshipAndOtherClaims extends Controller with CachedClaim with Navigable {
  val form = Form(
    mapping(
      "relationship" -> nonEmptyText(maxLength = 20),
      "armedForcesPayment" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(MoreAboutThePerson) { implicit claim =>
      Ok(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(form.fill(MoreAboutThePerson)(claim)))
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g3_relationshipAndOtherClaims(formWithErrors)),
      moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(routes.G7MoreAboutTheCare.present()))
  }
}