package controllers.circs.report_changes

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.circumstances.report_changes.GBreaksInCareSummaryPage
import controllers.CircumstancesScenarioFactory

class GBreaksInCareSummaryIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Breaks in care summary" should {
    "be presented" in new WithBrowser with PageObjects{
      val page =  GBreaksInCareSummaryPage(context)
      page goToThePage()
    }

    "navigate to next page when 'has this break from caring ended' yes and no additional breaks" in new WithBrowser with PageObjects {
      val page =  GBreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithNoAdditionalBreaks
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "navigate to next page when 'has this break from caring ended' yes and no additional breaks" in new WithBrowser with PageObjects {
      val page =  GBreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaks
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "show errors when additional breaks answer not specified" in new WithBrowser with PageObjects{
      val page =  GBreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaksNotAnswered
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(1)
    }

    "show errors when additional breaks detail not specified" in new WithBrowser with PageObjects{
      val page =  GBreaksInCareSummaryPage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringSummaryBreaksInCareEndedYesWithAdditionalBreaksButNotSpecified
      page goToThePage()
      page fillPageWith claim

      page submitPage ()
      page.listErrors.size must beEqualTo(1)
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
