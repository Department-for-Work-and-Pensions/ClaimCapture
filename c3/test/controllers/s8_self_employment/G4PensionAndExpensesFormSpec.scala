package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}

class G4PensionAndExpensesFormSpec extends Specification with Tags {
  "About Self Employment - Pension and Expenses Form" should {

    val yes = "yes"
    val no = "no"
    val pensionExpenses = "Some pension expenses"
    val jobExpenses = "Some job expenses"
    val overThreeHundredChars = "Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters,Characters"

    "map data into case class" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map(
          "payPensionScheme.answer" -> no,
          "haveExpensesForJob.answer" -> no
          )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.payPensionScheme.answer must equalTo(no)
          f.haveExpensesForJob.answer must equalTo(no)
        }
      )
    }

    "have 2 mandatory fields" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.text" -> pensionExpenses,
          "haveExpensesForJob.text" -> jobExpenses)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if haveExpensesForJob is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if payPensionScheme is not filled" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if payPensionScheme is yes" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> no,
          "payPensionScheme.answer" -> yes)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("payPensionScheme.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory field if haveExpensesForJob is yes" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("haveExpensesForJob.answer" -> yes,
          "payPensionScheme.answer" -> no)
      ).fold(

          formWithErrors =>  {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("haveExpensesForJob.text.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject too many characters in text fields" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> overThreeHundredChars,
          "haveExpensesForJob.text" -> overThreeHundredChars)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("error.maxLength")
            formWithErrors.errors(1).message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      G4SelfEmploymentPensionsAndExpenses.form.bind(
        Map("payPensionScheme.answer" -> yes,
          "haveExpensesForJob.answer" -> yes,
          "payPensionScheme.text" -> "<>",
          "haveExpensesForJob.text" -> "<>"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}