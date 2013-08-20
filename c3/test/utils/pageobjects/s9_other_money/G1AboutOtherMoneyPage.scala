package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G1AboutOtherMoneyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AboutOtherMoneyPage.url, G1AboutOtherMoneyPage.title, previousPage) {
  declareYesNo("#yourBenefits_answer", "OtherMoneyHaveYouClaimedOtherBenefits")
}

object G1AboutOtherMoneyPage {
  val title = "Details about other money - About Other Money".toLowerCase

  val url  = "/other-money/about-other-money"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1AboutOtherMoneyPage(browser,previousPage)
}

trait G1AboutOtherMoneyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutOtherMoneyPage buildPageWith browser
}