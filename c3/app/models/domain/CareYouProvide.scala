package models.domain

import models.{MultiLineAddress, Whereabouts, NationalInsuranceNumber, DayMonthYear}
import models.Postcode

object CareYouProvide {
  val id = "s4"
}

case class TheirPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                                nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends QuestionGroup(TheirPersonalDetails.id)

object TheirPersonalDetails {
  val id = s"${CareYouProvide.id}.g1"
}

case class TheirContactDetails(address: MultiLineAddress, postcode: Option[Postcode], phoneNumber: Option[String] = None) extends QuestionGroup(TheirContactDetails.id)

object TheirContactDetails {
  val id = s"${CareYouProvide.id}.g2"
}

case class HasBreaks(answer: String) extends QuestionGroup(HasBreaks.id)

object HasBreaks {
  val id = s"${CareYouProvide.id}.g10"
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare.id) {
  def update(break: Break) = BreaksInCare(breaks :+ break)

  def delete(breakID: String) = BreaksInCare(breaks.filterNot(_.id == breakID))
}

object BreaksInCare {
  val id = s"${CareYouProvide.id}.g11"

  def apply() = new BreaksInCare()
}

case class BreakInCare(moreBreaks: String, break: Option[Break])

case class Break(id: String, start: DayMonthYear, end: Option[DayMonthYear], whereYou: Whereabouts, wherePerson: Whereabouts, medicalDuringBreak: Option[String])
