package controllers

object PreviewRouteUtils {

  def yourDetailsRoute = {
    val gYourDetailsRoute = controllers.s_about_you.routes.GYourDetails.present.toString
    val gMaritalStatusRoute = controllers.s_about_you.routes.GMaritalStatus.present.toString
    val gContactDetailsRoute = controllers.s_about_you.routes.GContactDetails.present.toString
    val gNationalityRoute = controllers.s_about_you.routes.GNationalityAndResidency.present.toString
    val gOtherEEARoute = controllers.s_about_you.routes.GPaymentsFromAbraod.present.toString

    val routesMap = Map("about_you_full_name" -> gYourDetailsRoute,
                        "about_you_dob" -> gYourDetailsRoute,
                        "about_you_address" -> gContactDetailsRoute,
                        "about_you_contact" -> gContactDetailsRoute,
                        "about_you_email" -> gContactDetailsRoute,
                        "about_you_marital_status" -> gMaritalStatusRoute,
                        "about_you_claimDate" -> controllers.s_claim_date.routes.GClaimDate.present.toString,
                        "about_you_nationality" -> gNationalityRoute,
                        "about_you_alwaysliveinuk" -> gNationalityRoute,
                        "about_you_liveinuknow" -> gNationalityRoute,
                        "about_you_arrivedinuk" -> gNationalityRoute,
                        "about_you_arrivedinukdate" -> gNationalityRoute,
                        "about_you_arrivedinukfrom" -> gNationalityRoute,
                        "about_you_trip52weeks" -> gNationalityRoute,
                        "about_you_eeaGuardQuestion" -> gOtherEEARoute,
                        "about_you_benefitsFromEEA" -> gOtherEEARoute,
                        "about_you_workingForEEA" -> gOtherEEARoute)

    routesMap
  }

  def employmentRoute = {
    val employmentRoute = controllers.your_income.routes.GYourIncomes.present.toString
    val jobsRoute = controllers.s_employment.routes.GBeenEmployed.present.toString
    val selfEmploymentDates = controllers.s_self_employment.routes.GSelfEmploymentDates.present.toString
    val additionalInfoRoute = controllers.s_employment.routes.GEmploymentAdditionalInfo.present.toString
    val routesMap = Map(
      "your_income" -> employmentRoute,
      "employment_been_employed_since" -> employmentRoute,
      "employment_jobs" -> jobsRoute,
      "self_employment_been_self_employed" -> employmentRoute,
      "self_employment_dates" -> selfEmploymentDates,
      "employment_additional_info" -> additionalInfoRoute
    )
    routesMap
  }

  def yourIncomeOtherPaymentsRoute = {
    val statutorySickPayRoute = controllers.your_income.routes.GStatutorySickPay.present.toString
    val statutoryPayRoute = controllers.your_income.routes.GStatutoryMaternityPaternityAdoptionPay.present.toString
    val fosteringAllowanceRoute = controllers.your_income.routes.GFosteringAllowance.present.toString
    val directPayment = controllers.your_income.routes.GDirectPayment.present.toString
    val rentalIncome = controllers.your_income.routes.GRentalIncome.present.toString
    val otherPayments = controllers.your_income.routes.GOtherPayments.present.toString

    Map(
      "your_income_statutory_sick_pay" -> statutorySickPayRoute,
      "your_income_statutory_pay" -> statutoryPayRoute,
      "your_income_fostering_allowance" -> fosteringAllowanceRoute,
      "your_income_direct_payment" -> directPayment,
      "your_income_rental_income" -> rentalIncome,
      "your_income_other_payments" -> otherPayments
    )
  }

  def educationRoute = {
    val gYourCourseDetailsRoute = controllers.s_education.routes.GYourCourseDetails.present.toString
    val idList = Seq("education_beenInEducationSinceClaimDate", "education_courseTitle", "education_nameOfSchool",
      "education_nameOfTutor", "education_contactNumber", "education_startEndDates")

    val routesMap = Map(idList map {id => (id, gYourCourseDetailsRoute)} : _*)

    routesMap
  }

  def breaksRoute = {
    val route = controllers.breaks_in_care.routes.GBreaksInCareSummary.present.toString
    val list = Seq("breaks_hospital", "breaks_carehome", "breaks_breaktype_other", "breaks_breaktype")
    val routesMap = Map(list map{id => (id,route)}: _*)
    routesMap
  }

  def careYouProvide = {
    val gTheirPersonalDetailsRoute = controllers.s_care_you_provide.routes.GTheirPersonalDetails.present.toString
    val gMoreAboutTheCareRoute = controllers.s_care_you_provide.routes.GMoreAboutTheCare.present.toString

    val personalDetailsList = Seq("care_you_provide_name", "care_you_provide_dob", "care_you_provide_relationship")
    val contactDetailsList = Seq("care_you_provide_address")
    val moreAboutTheCareList = Seq("care_you_provide_spent35HoursCaring", "care_you_provide_otherCarer",
      "care_you_provide_otherCarerUc", "care_you_provide_otherCarerUcDetails")

    val routesMap = Map(personalDetailsList map {id => (id, gTheirPersonalDetailsRoute)} : _*) ++
                    Map(contactDetailsList map{id => (id, gTheirPersonalDetailsRoute)} : _*) ++
                    Map(moreAboutTheCareList map{id => (id, gMoreAboutTheCareRoute)} : _*)
    routesMap
  }

  def yourPartner = {
    val gYourPartnerPersonalDetailsRoute = controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present.toString

    val partnerDetailsList = Seq("partner_hadPartner", "partner_name",
      "partner_dateOfBirth", "partner_nationality", "partner_seperated", "partner_isPersonCareFor")

    val routesMap = Map(partnerDetailsList map {id => (id, gYourPartnerPersonalDetailsRoute)} : _*)

    routesMap
  }

  def bankDetailsRoute = {
    val gBankDetailsRoute = controllers.s_pay_details.routes.GHowWePayYou.present.toString

    val bankDetailsList = Seq("bank_details")
    val routesMap = Map(bankDetailsList map {id => (id, gBankDetailsRoute)} : _*)
    routesMap
  }

  def additionalInfoRoute = {
    val gAdditionalInfoRoute = controllers.s_information.routes.GAdditionalInfo.present.toString

    val additionalInfoList = Seq("additional_info", "additional_info_welsh")

    val routesMap = Map(additionalInfoList map {id => (id, gAdditionalInfoRoute)} : _*)
    routesMap
  }

  def thirdPartyRoute = {
    val gThirdPartyRoute = controllers.third_party.routes.GThirdParty.present.toString

    val thirdPartyList = Seq("third_party")
    val routesMap = Map(thirdPartyList map {id => (id, gThirdPartyRoute)} : _*)
    routesMap
  }
}
