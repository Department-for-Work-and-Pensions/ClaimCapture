package models.yesNo

/**
 * Created by neddakaltcheva on 2/14/14.
 */
import controllers.Mappings._

case class YesNoComplex(answer: String = "", address: YesNoWithAddress = YesNoWithAddress("", None, None))

object YesNoComplex {

  def validateOnYes(input: YesNoComplex): Boolean = input.answer match {
    case `yes` => input.address.answer.isDefinedAt(0)
    case `no` => true
  }

  def validateOnNo(input: YesNoComplex): Boolean = input.answer match {
    case `yes` => true
    case `no` => input.address.answer.isDefinedAt(0)
  }

  def validateAnswerNotEmpty(input: YesNoComplex): Boolean = !input.answer.isEmpty
}