package models.domain

import models._
import play.api.i18n.Messages
import scala.reflect.ClassTag
import controllers.Mappings._
import models.PaymentFrequency
import scala.Some
import models.PensionPaymentFrequency
import models.MultiLineAddress

object Employed extends Section.Identifier {
  val id = "s7"
}

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String = "", beenEmployedSince6MonthsBeforeClaim: String = "") extends QuestionGroup(Employment)

object Employment extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g0"
}

case class BeenEmployed(beenEmployed: String) extends QuestionGroup(BeenEmployed)

object BeenEmployed extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g1"
}

case class Jobs(jobs: List[Job] = Nil) extends QuestionGroup(Jobs) with Iterable[Job] {
  def update(job: Job): Jobs = {
    val updated = jobs map { j => if (j.jobID == job.jobID) job else j }

    if (updated.contains(job)) Jobs(updated) else Jobs(jobs :+ job)
  }

  def update(questionGroup: QuestionGroup with Job.Identifier): Jobs = jobs.find(_.jobID == questionGroup.jobID) match {
    case Some(job: Job) => update(job.update(questionGroup))
    case _ => update(Job(questionGroup.jobID, questionGroup :: Nil))
  }

  def completeJob(jobID: String): Jobs = {
    job(jobID) match {
        case Some(j:Job) => update(j.copy( completed = j.questionGroups.size > 5 ))
        case _ => copy()
      }
  }

  def delete(jobID: String): Jobs = Jobs(jobs.filterNot(_.jobID == jobID))

  def delete(jobID: String, questionGroup: QuestionGroup.Identifier): Jobs = jobs.find(_.jobID == jobID) match {
    case Some(job: Job) => update(Job(jobID,job.questionGroups.filterNot(_.identifier.id == questionGroup.id)))
    case _ => this
  }

  def job(jobID: String): Option[Job] = jobs.find(_.jobID == jobID)

  def questionGroup(questionGroup: QuestionGroup with Job.Identifier): Option[QuestionGroup] = job(questionGroup.jobID) match {
    case Some(j: Job) => j(questionGroup)
    case _ => None
  }

  def questionGroup(jobID: String, questionGroup: QuestionGroup.Identifier): Option[QuestionGroup] = job(jobID) match {
    case Some(j: Job) => j(questionGroup)
    case _ => None
  }

  override def iterator: Iterator[Job] = jobs.iterator
}

object Jobs extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g99"
}

case class Job(jobID: String="", questionGroups: List[QuestionGroup with Job.Identifier] = Nil, completed:Boolean=false) extends Job.Identifier with Iterable[QuestionGroup with Job.Identifier] {
  def employerName = jobDetails(_.employerName)

  def title = necessaryExpenses(_.jobTitle)

  def jobStartDate = jobDetailsDate(_.jobStartDate)

  def update(questionGroup: QuestionGroup with Job.Identifier): Job = {
    val updated = questionGroups map { qg => if (qg.identifier == questionGroup.identifier) questionGroup else qg }
    if (updated.contains(questionGroup)) copy(questionGroups = updated) else copy(questionGroups = questionGroups :+ questionGroup)
  }

  private def jobDetails(f: JobDetails => String) = questionGroups.find(_.isInstanceOf[JobDetails]) match {
    case Some(j: JobDetails) => f(j)
    case _ => ""
  }

  private def necessaryExpenses(f: NecessaryExpenses => String) = questionGroups.find(_.isInstanceOf[NecessaryExpenses]) match {
    case Some(n: NecessaryExpenses) => f(n)
    case _ => ""
  }

  private def jobDetailsDate(f:JobDetails => DayMonthYear) = questionGroups.find(_.isInstanceOf[JobDetails]) match {
    case Some(j: JobDetails) => f(j)
    case _ => DayMonthYear(None,None,None)
  }

  def questionGroup[Q <: QuestionGroup](implicit classTag: ClassTag[Q]): Option[Q] = {
    def needQ(qg: QuestionGroup): Boolean = {
      qg.getClass == classTag.runtimeClass
    }

    questionGroups.find(needQ) match {
      case Some(q: Q) => Some(q)
      case _ => None
    }
  }

  def apply(questionGroup: QuestionGroup): Option[QuestionGroup] = questionGroups.find(_.identifier == questionGroup.identifier)

  def apply(questionGroup: QuestionGroup.Identifier): Option[QuestionGroup] = questionGroups.find(_.identifier.id == questionGroup.id)

  override def iterator: Iterator[QuestionGroup with Job.Identifier] = questionGroups.iterator
}

object Job {
  trait Identifier {
    val jobID: String
  }
}

case class JobDetails(jobID: String = "",
                      employerName: String = "",
                      jobStartDate: DayMonthYear = DayMonthYear(None, None, None),
                      finishedThisJob: String = "",
                      lastWorkDate:Option[DayMonthYear] = None,
                      p45LeavingDate:Option[DayMonthYear] = None,
                      hoursPerWeek: Option[String] = None,
                      payrollEmployeeNumber: Option[String] = None) extends QuestionGroup(JobDetails) with Job.Identifier {
  override val definition = Messages(identifier.id, employerName)
}

object JobDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g2"

  def validateLastWorkDate(input: JobDetails):Boolean = input.finishedThisJob match {
    case `yes` => input.lastWorkDate.isDefined
    case `no` => true
  }
}

case class EmployerContactDetails(jobID: String = "",
                                  address: MultiLineAddress = MultiLineAddress(),
                                  postcode: Option[String] = None,
                                  phoneNumber: Option[String] = None) extends QuestionGroup(EmployerContactDetails) with Job.Identifier

object EmployerContactDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g3"
}

case class LastWage(jobID: String = "",
                    lastPaidDate: Option[DayMonthYear] = None,
                    grossPay: String = "",
                    payInclusions: Option[String] = None,
                    sameAmountEachTime: Option[String] = None) extends QuestionGroup(LastWage) with Job.Identifier

object LastWage extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g4"
}

case class AdditionalWageDetails(jobID:String = "",
                                 oftenGetPaid: PaymentFrequency = PaymentFrequency(),
                                 whenGetPaid: Option[String] = None,
                                 employerOwesYouMoney: String = "") extends QuestionGroup(AdditionalWageDetails) with Job.Identifier

object AdditionalWageDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g5"

  def validateOftenGetPaid(input: AdditionalWageDetails): Boolean = input.oftenGetPaid match {
    case payment if payment.frequency == "other" => payment.other.isDefined
    case _ => true
  }
}

case class PensionSchemes(jobID: String = "",
                          payOccupationalPensionScheme: String = "",
                          howMuchPension: Option[String] = None,
                          howOftenPension: Option[PensionPaymentFrequency] = None,
                          payPersonalPensionScheme: String = "",
                          howMuchPersonal: Option[String] = None,
                          howOftenPersonal: Option[PensionPaymentFrequency] = None) extends QuestionGroup(PensionSchemes) with Job.Identifier

object PensionSchemes extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g7"

  def validateHowMuchPension(input: PensionSchemes): Boolean = input.payOccupationalPensionScheme match {
    case `yes` => input.howMuchPension.isDefined
    case `no` => true
  }

  def validateHowOftenPension(input: PensionSchemes): Boolean = input.payOccupationalPensionScheme match {
    case `yes` => input.howOftenPension.isDefined
    case `no` => true
  }

  def validateHowMuchPersonal(input: PensionSchemes): Boolean = input.payPersonalPensionScheme match {
    case `yes` => input.howMuchPersonal.isDefined
    case `no` => true
  }

  def validateHowOftenPersonal(input: PensionSchemes): Boolean = input.payPersonalPensionScheme match {
    case `yes` => input.howOftenPersonal.isDefined
    case `no` => true
  }
}

case class AboutExpenses(jobID: String = "",
                         payForAnythingNecessary: String = "",
                         payAnyoneToLookAfterChildren: String = "",
                         payAnyoneToLookAfterPerson: String = "") extends QuestionGroup(AboutExpenses) with Job.Identifier

object AboutExpenses extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g8"
}

case class NecessaryExpenses(jobID: String = "",
                             jobTitle: String = "",
                             whatAreThose: String = "") extends QuestionGroup(NecessaryExpenses) with Job.Identifier

object NecessaryExpenses extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g9"
}

case class ChildcareExpenses(jobID: String = "",
                             whoLooksAfterChildren: String = "",
                             howMuchCostChildcare: String = "",
                             howOftenPayChildCare: PensionPaymentFrequency = models.PensionPaymentFrequency(""),
                             relationToYou: String = "",
                             relationToPartner: Option[String] = None,
                             relationToPersonYouCare: String = "") extends QuestionGroup(ChildcareExpenses) with Job.Identifier

object ChildcareExpenses extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g10"
}

case class PersonYouCareForExpenses(jobID: String = "",
                                    whoDoYouPay: String = "",
                                    howMuchCostCare: String = "",
                                    howOftenPayCare: PensionPaymentFrequency = models.PensionPaymentFrequency(""),
                                    relationToYou: String = "",
                                    relationToPersonYouCare: String = "") extends QuestionGroup(PersonYouCareForExpenses) with Job.Identifier

object PersonYouCareForExpenses extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g12"
}

object JobCompletion extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g14"
}