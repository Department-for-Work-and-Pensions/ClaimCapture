package app.circumstances

import app.FunctionalTestCommon
import utils.WithJsBrowser
import utils.pageobjects.{Page, XmlPage, TestData, PageObjects}
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.pageobjects.xml_validation.{XMLCircumstancesBusinessValidation, XMLBusinessValidation}

/**
 * Created by neddakaltcheva on 4/14/14.
 */
class FunctionalTestCase26Spec extends FunctionalTestCommon {
  isolated

  section("functional")
  "The application Circumstances" should {
    "Successfully run absolute Circumstances Test Case 26 for change of address" in new WithJsBrowser with PageObjects {
      val page = GReportChangesPage(context)
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase26.csv")
      page goToThePage()

      val lastPage = page runClaimWith(circs)

      lastPage match {
        case p: XmlPage => {
          val validator: XMLBusinessValidation = new XMLCircumstancesBusinessValidation
          validateAndPrintErrors(p, circs, validator) should beTrue
        }
        case p: Page => println(p.source)
      }
    }
  }
  section("functional")
}
