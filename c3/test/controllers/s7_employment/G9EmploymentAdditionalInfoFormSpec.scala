package controllers.s7_employment

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import models.yesNo.YesNoWithText
import controllers.mappings.Mappings


class G9EmploymentAdditionalInfoFormSpec extends Specification with Tags{

  val additionalInfoYes = YesNoWithText("yes", Some("No more information"))
  val additionalInfoNo = YesNoWithText("no", None)

  val textValue = "1234567890"

  def maxCharactersString = {
    var result = ""
    for (i <- 1 to 300) result += s"$textValue"
    result
  }

  "Employment - Additional Info" should {

    "must have 1 mandatory field" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map( "" -> "")
      ).fold(
        formWithErrors =>
        {
          formWithErrors.errors.length mustEqual 1
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "must have 1 mandatory field when answered yes" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map( "empAdditionalInfo.answer" -> additionalInfoYes.answer)
      ).fold(
        formWithErrors => formWithErrors.errors.length mustEqual 1,
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "map data into case class" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map(
          "empAdditionalInfo.answer" -> additionalInfoYes.answer,
          "empAdditionalInfo.text" -> additionalInfoYes.text.get
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.empAdditionalInfo mustEqual additionalInfoYes
        }
      )
    }

    "map data into case class when answered no" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map(
          "empAdditionalInfo.answer" -> additionalInfoNo.answer
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.empAdditionalInfo mustEqual additionalInfoNo
        }
      )
    }

    "not map into case class when special characters entered" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map(
          "empAdditionalInfo.answer" -> additionalInfoYes.answer,
          "empAdditionalInfo.text" -> "{}"
        )
      ).fold(
        formWithErrors => formWithErrors.errors.length mustEqual 1,
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "not map into case class when more than 3000 characters entered" in {
      G9EmploymentAdditionalInfo.form.bind(
        Map(
          "empAdditionalInfo.answer" -> additionalInfoYes.answer,
          "empAdditionalInfo.text" -> maxCharactersString.concat(" ") // this will be 3001 characters
        )
      ).fold(
        formWithErrors => formWithErrors.errors.length mustEqual 1,
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}
