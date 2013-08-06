package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G7PensionSchemesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G7PensionSchemesPage.url, G7PensionSchemesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

    declareYesNo("#payOccupationalPensionScheme", "EmploymentDoYouPayTowardsanOccupationalPensionScheme_"+iteration)
    declareInput("#howMuchPension", "EmploymentHowMuchYouPayforOccupationalPension_"+iteration)
    declareSelect("#howOftenPension", "EmploymentHowOftenOccupationalPension_"+iteration)
    declareYesNo("#payPersonalPensionScheme", "EmploymentDoYouPayTowardsAPersonalPension_"+iteration)
    declareInput("#howMuchPersonal", "EmploymentHowMuchYouPayforPersonalPension_"+iteration)
    declareSelect("#howOftenPersonal", "EmploymentHowOftenPersonalPension_"+iteration)
  
}

object G7PensionSchemesPage {
  val title = "Pension schemes - Employment"
  val url  = "/employment/pensionSchemes/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G7PensionSchemesPage(browser,previousPage,iteration)
}

trait G7PensionSchemesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G7PensionSchemesPage buildPageWith(browser,iteration = 1)
}
