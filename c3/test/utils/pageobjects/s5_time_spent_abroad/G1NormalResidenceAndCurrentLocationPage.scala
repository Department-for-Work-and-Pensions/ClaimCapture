package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g1_normal_residence_and_current_location.
 * @author Saqib Kayani
 *         Date: 30/07/2013
 */
final class G1NormalResidenceAndCurrentLocationPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1NormalResidenceAndCurrentLocationPage.url, G1NormalResidenceAndCurrentLocationPage.title, previousPage) {

    declareYesNo("#liveInUK_answer", "TimeSpentAbroadDoYouNormallyLiveintheUk")
    declareInput("#liveInUK_whereDoYouLive", "TimeSpentAbroadWhereDoYouNormallyLive")
    declareYesNo("#inGBNow", "TimeSpentAbroadAreYouinGBNow")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1NormalResidenceAndCurrentLocationPage {
  val title = "Normal Residence and Current Location - Time Spent Abroad"
  val url  = "/timeSpentAbroad/normalResidenceAndCurrentLocation"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1NormalResidenceAndCurrentLocationPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1NormalResidenceAndCurrentLocationPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1NormalResidenceAndCurrentLocationPage buildPageWith browser
}
