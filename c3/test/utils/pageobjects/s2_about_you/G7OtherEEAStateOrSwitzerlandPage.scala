package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * PageObject pattern associated to S7 about you EEA pension and insurance.
 */
final class G7OtherEEAStateOrSwitzerlandPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G7OtherEEAStateOrSwitzerlandPage.url, G7OtherEEAStateOrSwitzerlandPage.title, previousPage) {
  declareYesNo("#benefitsFromEEA","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareYesNo("#claimedForBenefitsFromEEA","OtherMoneyOtherAreYouClaimingForBenefitsFromAnotherEEA")
  declareYesNo("#workingForEEA", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
}

object G7OtherEEAStateOrSwitzerlandPage {
  val title = "Money you get from other European Economic Area (EEA) state or Switzerland - About you - the carer".toLowerCase

  val url = "/about-you/other-eea-state-or-switzerland"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G7OtherEEAStateOrSwitzerlandPage(browser, previousPage)

}

trait G7OtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7OtherEEAStateOrSwitzerlandPage (browser)
}