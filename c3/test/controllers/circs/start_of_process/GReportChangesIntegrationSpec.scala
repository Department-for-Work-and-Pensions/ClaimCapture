package controllers.circs.start_of_process

import app.ReportChange._
import controllers.CircumstancesScenarioFactory
import org.openqa.selenium.By
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.circumstances.start_of_process.{GCircsYourDetailsPage, GReportChangesPage}
import utils.pageobjects.circumstances.report_changes.{GOtherChangeInfoPage, _}
import CircumstancesScenarioFactory._

class GReportChangesIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GReportChangesPage(context)
      page goToThePage()
    }

    "navigate to next page when addition info selected" in new WithBrowser with PageObjects {
      val page = GReportChangesPage(context)
      val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GCircsYourDetailsPage]
    }

    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = GReportChangesPage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }

    "Not save 2 mutually exclusive reasons - see DE944" in new WithBrowser with PageObjects {

      // selects one reason and submits it to the last page so it gets saved
      // then selects a different reason and checks that the data refering to "you" is still saved
      // then it selects the same initial reason and checks that the data input initially was cleared by the second step

      val reportChangesFirst =  GReportChangesPage(context)
      reportChangesFirst goToThePage()

      val otherChangeInfoPageFirst = reportChangesFirst runClaimWith (reportChangesOtherChangeInfo, GOtherChangeInfoPage.url)
      otherChangeInfoPageFirst must beAnInstanceOf[GOtherChangeInfoPage]
      otherChangeInfoPageFirst.fillInput("#changeInCircs", "bla bla")

      //submit to the last page and go back to the selectionPage
      val lastPage = otherChangeInfoPageFirst.submitPage()
      val reportChangesSecond = lastPage.goBack().goBack().goBack()
      reportChangesSecond must beAnInstanceOf[GReportChangesPage]


      //select a different Ch of circs
      val justAddress = new TestData
      justAddress.CircumstancesReportChanges = AddressChange.name
      val addressPage = reportChangesSecond runClaimWith (justAddress, GAddressChangePage.url)
      //verifies that the second page was already filled
      addressPage must beAnInstanceOf[GAddressChangePage]


      //select the first Ch of circs again - and verify that it's value was cleared by the previous step
      val reportChangesThird = addressPage.goBack().goBack()
      reportChangesThird must beAnInstanceOf[GReportChangesPage]

      val justOtherInfo = new TestData
      justOtherInfo.CircumstancesReportChanges = AdditionalInfo.name
      val otherChangeInfoPageSecond = reportChangesThird runClaimWith (justOtherInfo, GOtherChangeInfoPage.url)
      otherChangeInfoPageSecond must beAnInstanceOf[GOtherChangeInfoPage]

      //verify that the other field is not filled
      otherChangeInfoPageSecond.ctx.browser.getDriver.findElement(By.id("changeInCircs")).getText must_== ""
      //the submit fails with validation
      val samePage = otherChangeInfoPageSecond.submitPage()
      samePage must beAnInstanceOf[GOtherChangeInfoPage]
    }

    "Not remove reasons when no change to original selection" in new WithBrowser with PageObjects {

      // selects one reason and submits it to the last page
      // then it selects the same initial reason and checks that the data input initially was not cleared by the second step

      val reportChangesFirst =  GReportChangesPage(context)
      reportChangesFirst goToThePage()

      val otherChangeInfoPageFirst = reportChangesFirst runClaimWith (reportChangesOtherChangeInfo, GOtherChangeInfoPage.url)
      otherChangeInfoPageFirst must beAnInstanceOf[GOtherChangeInfoPage]
      otherChangeInfoPageFirst.fillInput("#changeInCircs", "bla bla")

      //submit to the last page and go back to the selectionPage
      val lastPage = otherChangeInfoPageFirst.submitPage()
      val reportChangesSecond = lastPage.goBack().goBack().goBack()
      reportChangesSecond must beAnInstanceOf[GReportChangesPage]


      val justOtherInfo = new TestData
      justOtherInfo.CircumstancesReportChanges = AdditionalInfo.name
      val otherChangeInfoPageSecond = reportChangesSecond runClaimWith (justOtherInfo, GOtherChangeInfoPage.url)
      otherChangeInfoPageSecond must beAnInstanceOf[GOtherChangeInfoPage]

      //verify that the other field is filled
      otherChangeInfoPageSecond.ctx.browser.getDriver.findElement(By.id("changeInCircs")).getText must_== "bla bla"
      //the submit is valid
      val samePage = otherChangeInfoPageSecond.submitPage()
      samePage must beAnInstanceOf[GCircsDeclarationPage]
    }
  }
  section("integration", models.domain.CircumstancesReportChanges.id)
}
