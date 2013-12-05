package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G5CompletedIntegrationSpec extends Specification with Tags {

  "Your Partner" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-partner/completed")
      titleMustEqual("Completion - About your partner/spouse")
    }

    """navigate to "Care you provide" page.""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-partner/completed")
      browser.submit("button[type='submit']")
      browser.find("#submit").getText mustEqual "Continue to Care you provide"
      titleMustEqual("Details of the person you care for - About the care you provide")
    }
    
    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Completion - About your partner/spouse")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

  } section("integration", models.domain.YourPartner.id)
}