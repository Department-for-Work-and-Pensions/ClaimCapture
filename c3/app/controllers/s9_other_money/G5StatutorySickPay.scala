package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.StatutorySickPay
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G5StatutorySickPay extends Controller with OtherMoneyRouting with CachedClaim {
  val form = Form(
    mapping(
      "haveYouHadAnyStatutorySickPay" -> nonEmptyText(maxLength = sixty),
      "howMuch" -> optional(nonEmptyText verifying(validDecimalNumber)),
      "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
      "employersName" -> optional(nonEmptyText(maxLength = sixty)),
      "employersAddress" -> optional(address),
      "employersPostcode" -> optional(text verifying validPostcode)
    )(StatutorySickPay.apply)(StatutorySickPay.unapply)
      .verifying("employersName.required", validateEmployerName _))

  def validateEmployerName(statutorySickPay: StatutorySickPay) = {
    statutorySickPay.haveYouHadAnyStatutorySickPay match {
      case `yes` => statutorySickPay.employersName.isDefined
      case _ => true
    }
  }

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_other_money.g5_statutorySickPay(form.fill(StatutorySickPay), completedQuestionGroups(StatutorySickPay)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("", "employersName.required", FormError("employersName", "error.required"))
        BadRequest(views.html.s9_other_money.g5_statutorySickPay(formWithErrorsUpdate, completedQuestionGroups(StatutorySickPay)))
      },
      f => claim.update(f) -> Redirect(routes.G6OtherStatutoryPay.present()))
  }
}