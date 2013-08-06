package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G4PersonContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4PersonContactDetailsPage.url, G4PersonContactDetailsPage.title, previousPage) {


    declareAddress("#address", "OtherMoneyOtherPersonAddress")
    declareInput("#postcode", "OtherMoneyOtherPersonPostcode")

}

object G4PersonContactDetailsPage {
  val title = "Contact Details - Other Money"
  val url = "/otherMoney/personContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4PersonContactDetailsPage(browser, previousPage)
}

trait G4PersonContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G4PersonContactDetailsPage buildPageWith browser
}
