package xml

import scala.xml._
import scala.language.implicitConversions
import scala.reflect.ClassTag
import app.StatutoryPaymentFrequency
import app.XMLValues._
import models._
import models.PaymentFrequency
import models.MultiLineAddress
import models.PeriodFromTo
import models.NationalInsuranceNumber
import play.api.i18n.Messages

object XMLHelper {

  def stringify(value: Option[_], default: String = ""): String = value match {
    case Some(s: String) => s
    case Some(dmy: DayMonthYear) => dmy.`dd-MM-yyyy`
    case Some(nr: NationalInsuranceNumber) => nr.stringify
    case Some(sc: SortCode) => sc.stringify
    case Some(pf: PaymentFrequency) => pf.stringify
    case _ => default
  }

  def nodify(value: Option[_]): NodeBuffer = value match {
    case Some(s: String) => new NodeBuffer += Text(s)
    case Some(dmy: DayMonthYear) => new NodeBuffer += Text(dmy.`dd-MM-yyyy`)
    case Some(nr: NationalInsuranceNumber) => new NodeBuffer += Text(nr.stringify)
    case Some(pf: PaymentFrequency) => paymentFrequency(pf)
    case Some(pft: PeriodFromTo) => fromToStructure(pft)
    case Some(sc: SortCode) => new NodeBuffer += Text(sc.sort1 + sc.sort2 + sc.sort2)
    case _ => new NodeBuffer += Text("")
  }

  def postalAddressStructure(addressOption: Option[MultiLineAddress], postcodeOption: Option[String]): NodeSeq = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(address, postcodeOption.orNull)
    case _ => postalAddressStructure(new MultiLineAddress(), postcodeOption.orNull)
  }

  def postalAddressStructure(addressOption: MultiLineAddress, postcodeOption: Option[String]): NodeSeq = postalAddressStructure(addressOption, postcodeOption.getOrElse(""))

  def postalAddressStructure(address: MultiLineAddress, postcode: String): NodeSeq = {
    <Address>
      {if(address.lineOne.isEmpty){NodeSeq.Empty}else{<Line>{address.lineOne.orNull}</Line>}}
      {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.orNull}</Line>}}
      {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.orNull}</Line>}}
      {if(postcode == null || postcode == "") NodeSeq.Empty else <PostCode>{postcode.toUpperCase}</PostCode>}
    </Address>
  }

  def postalAddressStructureRecipientAddress(address: MultiLineAddress, postcode: String): NodeSeq = {
    <RecipientAddress>
      <Line>{address.lineOne.orNull}</Line>
      {if(address.lineTwo.isEmpty){NodeSeq.Empty}else{<Line>{address.lineTwo.orNull}</Line>}}
      {if(address.lineThree.isEmpty){NodeSeq.Empty}else{<Line>{address.lineThree.orNull}</Line>}}
      {if(postcode == null || postcode == "") NodeSeq.Empty else <PostCode>{postcode.toUpperCase}</PostCode>}
    </RecipientAddress>
  }

  def moneyStructure(amount: String) = {
    <Currency>{GBP}</Currency>
    <Amount>{amount}</Amount>
  }


  def question(questionLabelCode: String, answerText: String): NodeSeq = {
    val questionNode = <QuestionLabel>{Messages(questionLabelCode)}</QuestionLabel>
    questionNode ++ <Answer>{formatValue(answerText)}</Answer>
  }

  def questionOther(questionLabelCode: String, answerText: String, otherText: Option[String]): NodeSeq = {
    val other = <Other/>
    val questionNode = <QuestionLabel>{Messages(questionLabelCode)}</QuestionLabel>
    questionNode ++ <Answer>{formatValue(answerText)}</Answer> ++ optionalEmpty(otherText,other)
  }

  def fromToStructure(period: Option[PeriodFromTo]): NodeBuffer = {
    period.fold(
        <DateFrom/>
          <DateTo/>
    )(fromToStructure)
  }

  def fromToStructure(period: PeriodFromTo): NodeBuffer = {
    <DateFrom>{period.from.`yyyy-MM-dd`}</DateFrom>
    <DateTo>{period.to.`yyyy-MM-dd`}</DateTo>
  }

  def paymentFrequency(freq: Option[PaymentFrequency]): NodeBuffer = freq match {
    case Some(p) => paymentFrequency(p)
    case _ => new NodeBuffer()
  }

  def paymentFrequency(freq: PaymentFrequency): NodeBuffer = new NodeBuffer() +=
    <PayFrequency><QuestionLabel>job.pay.frequency</QuestionLabel><Answer>{StatutoryPaymentFrequency.mapToHumanReadableString(freq.frequency,None)}</Answer></PayFrequency>

  def optional[T](option: Option[T], elem: Elem)(implicit classTag: ClassTag[T]): Elem = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => elem
  }

  def optional[T](option: Option[T], elem: Elem, default: String)(implicit classTag: ClassTag[T]): Elem = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => addChild(elem, Text(default))
  }

  def optionalEmpty[T](option: Option[T], elem: Elem)(implicit classTag: ClassTag[T]): NodeSeq = option match {
    case Some(o) => addChild(elem, nodify(option))
    case _ => NodeSeq.Empty
  }

  def addChild(n: Node, children: NodeSeq) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) => Elem(prefix, label, attribs, scope, true, child ++ children : _*)
    case _ => <error>failed adding children</error>
  }

  implicit def attachToNode(elem: Elem) = new {
    def +++[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem)

    def +-[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, NotAsked)

    def +?[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, yes)

    def +!?[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = optional(option, elem, no)

    def ?+[T](option: Option[T])(implicit classTag: ClassTag[T]): NodeSeq = optionalEmpty(option, elem)
  }

  def booleanStringToYesNo(booleanString: String) = booleanString match {
    case "true" => yes
    case "false" => no
    case null => ""
    case _ => booleanString
  }

  def titleCase(s: String) = if(s != null && s.length() > 0) s.head.toUpper + s.tail.toLowerCase else ""

  def formatValue(value:String):String = value match {
       case "yes" => Yes
       case "no" => No
       case "other" => Other
       case _ => value
     }

  def extractIdFrom(xml:Elem):String = {(xml \\ "TransactionId").text}

}