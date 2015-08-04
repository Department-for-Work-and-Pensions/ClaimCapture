package controllers.s_your_partner

import play.api.mvc.{Request, AnyContent, Controller}
import models.view.CachedClaim
import models.view.Navigable
import models.domain.Claim
import play.api.i18n.Lang
import models.view.ClaimHandling.ClaimResult

object YourPartner extends Controller with CachedClaim with Navigable {

  def presentConditionally(c: => ClaimResult, lang: Lang) (implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourPartner.visible){
      c
    } else {
      redirect(lang)
    }
  }

  private def redirect(lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s_care_you_provide.routes.GTheirPersonalDetails.present())
}