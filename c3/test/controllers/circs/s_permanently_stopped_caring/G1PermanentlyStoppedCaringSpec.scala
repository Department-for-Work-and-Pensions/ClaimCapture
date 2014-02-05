package controllers.circs.s2_additional_info

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import models.view.CachedChangeOfCircs
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import models.domain.Claim
import scala.Some
import models.DayMonthYear
import controllers.circs.s2_report_changes.G1PermanentlyStoppedCaring
import controllers.circs.s2_report_changes


class G1PermanentlyStoppedCaringSpec extends Specification with Tags{

  val moreAboutChanges = "more about the change"
  val stoppedCaringDateDay = 23
  val stoppedCaringDateMonth = 12
  val stoppedCaringDateYear = 2013

  val stoppedCaringInput = Seq("moreAboutChange" -> moreAboutChanges,
    "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
    "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
    "stoppedCaringDate.year" -> stoppedCaringDateYear.toString
  )

  "Circumstances - PermanentlyStoppedCaring - Controller" should {

    "present 'Permanently Stopped Caring' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G1PermanentlyStoppedCaring.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(stoppedCaringInput: _*)

      val result = s2_report_changes.G1PermanentlyStoppedCaring.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.CircumstancesPermanentlyStoppedCaring.id)

}
