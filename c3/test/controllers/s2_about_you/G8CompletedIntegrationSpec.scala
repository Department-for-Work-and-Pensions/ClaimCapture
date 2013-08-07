package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G8CompletedIntegrationSpec extends Specification with Tags {
  "About You" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/aboutyou/completed")
      titleMustEqual ("Completion - About You")
    }
    
    """navigate to "Your Partner" when next section is "Your Partner"""" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      titleMustEqual("Completion - About You")
      
      browser.find("#submit").getText mustEqual "Continue to Your Partner/Spouse"
      browser.submit("button[type='submit']")
      titleMustEqual("Personal Details - Your Partner")
    }
    
    """navigate to "Care You Provide" when next section is "Care You Provide"""" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      titleMustEqual("Completion - About You")

      browser.find("#submit").getText mustEqual "Continue to Care you provide"
      browser.submit("button[type='submit']")
      titleMustEqual("Their Personal Details - Care You Provide")
    }
  } section "integration"
}