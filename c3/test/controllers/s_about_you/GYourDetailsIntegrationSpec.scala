package controllers.s_about_you

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.common.ClaimHelpPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you._
import utils.pageobjects.s_eligibility.GApprovePage
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.helpers.PreviewField._

class GYourDetailsIntegrationSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Your Details" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      page goToThePage()
    }

    "navigate back to approve page" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      browser goTo GApprovePage.url

      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[GApprovePage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 5
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

			val page =  claimDatePage submitPage()
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage()
      g2 must beAnInstanceOf[GMaritalStatusPage]
    }

    "Modify title, name, middlename and last name from preview page" in new WithBrowser with PageObjects {
      val id = "about_you_full_name"

      val answerText = PreviewTestUtils.answerText(id, _: Page)

      val previewPage = goToPreviewPage(context)
      answerText(previewPage) mustEqual "Mr John Appleseed"
      val aboutYou = previewPage.clickLinkOrButton(getLinkId(id))

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouTitle = "Mrs"
      modifiedData.AboutYouFirstName = "Jane"
      modifiedData.AboutYouSurname = "Pearson"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Mrs Jane Pearson"
    }

    "Modify date of birth from preview page" in new WithBrowser with PageObjects {
      val previewPage = goToPreviewPage(context)
      val id = "about_you_dob"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "03 April, 1950"
      val aboutYou = previewPage.clickLinkOrButton(getLinkId(id))

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouDateOfBirth = "03/04/1952"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "03 April, 1952"
    }

    "Strip whitespace from start and end of strings entered" in new WithBrowser with PageObjects{
      val id = "about_you_full_name"
      val answerText = PreviewTestUtils.answerText(id, _: Page)
      val previewPage = goToPreviewPage(context)
      answerText(previewPage) mustEqual "Mr John Appleseed"
      val aboutYou = previewPage.clickLinkOrButton(getLinkId(id))

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouTitle = "Mrs"
      modifiedData.AboutYouFirstName = "  Jane   "
      modifiedData.AboutYouSurname = "\tPearson"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()
      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Mrs Jane Pearson"
    }
  }
  section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val page =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    page goToThePage()
    page fillPageWith claim
    page submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }
}
