package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G1AdditionalInformationSpec extends Specification with Tags {
  "Additional Information" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)

      val result = G1AdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)

      val result = G1AdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
                                 .withFormUrlEncodedBody("welshCommunication" -> "yes")

      val result = G1AdditionalInfo.submit(request)
      redirectLocation(result) must beSome("/consent-and-declaration/consent")
    }
  } section("unit", models.domain.ConsentAndDeclaration.id)
}