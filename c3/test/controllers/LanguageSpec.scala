package controllers

import org.specs2.mutable._
import utils.WithApplication
import play.api.test.FakeRequest
import models.view._
import models.domain.{Claim, MockForm}
import play.api.test.Helpers._
import play.api.i18n.Lang

class LanguageSpec extends Specification {
  section("unit")
  "Language - Change Language" should {
    "change of claim present - change language to English" in new WithApplication with MockForm {
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey)
      cacheHandler.saveInCache(Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, "gacid", claimKey))

      val expectedLangAsString = "en"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = cacheHandler.fromCache(request).get
      claim.lang.get mustEqual expectedLang
    }

    "change of circs present - change language to English" in new WithApplication with MockForm {
      val cacheHandler = new CacheHandlingWithCircs()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey).withHeaders(REFERER -> "http://localhost:9000/circumstances/identification/about-you")
      cacheHandler.saveInCache(Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, "gacid", claimKey))

      val expectedLangAsString = "en"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = cacheHandler.fromCache(request).get
      claim.lang.get mustEqual expectedLang
    }

    "change of claim present - change language to Welsh" in new WithApplication with MockForm {
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey)
      cacheHandler.saveInCache(Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, "gacid", claimKey))

      val expectedLangAsString = "cy"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = cacheHandler.fromCache(request).get
      claim.lang.get mustEqual expectedLang
    }

    "change of circs present - change language to Welsh" in new WithApplication with MockForm {
      val cacheHandler = new CacheHandlingWithCircs()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey).withHeaders(REFERER -> "http://localhost:9000/circumstances/identification/about-you")
      cacheHandler.saveInCache(Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, "gacid", claimKey))

      val expectedLangAsString = "cy"
      val expectedLang = Lang(expectedLangAsString)
      val result = controllers.Language.change(expectedLangAsString)(request)
      status(result) mustEqual SEE_OTHER

      val claim = cacheHandler.fromCache(request).get
      claim.lang.get mustEqual expectedLang
    }
  }
  section("unit")
}
