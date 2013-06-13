package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G1YourDetailsSpec extends Specification with Tags {

  "Your Details" should{
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")
      browser.title() mustEqual "Your Details - About You"
    }
  } section "integration"
}