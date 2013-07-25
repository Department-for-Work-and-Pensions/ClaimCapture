package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.ClaimScenario
import scala.Some

class G4LivesInGBIntegrationSpec extends Specification with Tags {

  "LivesInGB" should {
    "be presented" in new WithBrowser with G4LivingInGBPageContext {
     page goToThePage()
    }
  } section "integration"

  "Do you normally live in Great Britain" should {
    "acknowledge yes" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage()
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage()
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage()
      livingGBPage must beAnInstanceOf[G4LivingInGBPage]
      livingGBPage.previousPage mustEqual Some(over16Page)
      livingGBPage fillPageWith claim
      livingGBPage submitPage()
      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage(waitDuration = 60)
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage(waitDuration = 60)
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage(waitDuration = 60)
      livingGBPage fillPageWith claim
      livingGBPage submitPage(waitDuration = 60)

      browser.find("div[class=completed] ul li").get(3).getText must contain("Q4")
      browser.find("div[class=completed] ul li").get(3).getText must contain("No")
    }

  } section "integration"
}