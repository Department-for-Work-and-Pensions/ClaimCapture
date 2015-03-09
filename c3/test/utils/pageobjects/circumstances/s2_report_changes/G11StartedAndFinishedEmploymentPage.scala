package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.CircumstancesPage
import utils.pageobjects.PageContext
import utils.pageobjects.PageObjectsContext
import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

final class G11StartedAndFinishedEmploymentPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G11StartedAndFinishedEmploymentPage.url, G11StartedAndFinishedEmploymentPage.title) {
  declareYesNo("#beenPaidYet", "CircumstancesEmploymentChangeBeenPaidYet")
  declareInput("#howMuchPaid", "CircumstancesEmploymentChangeHowMuchPaid")
  declareDate("#dateLastPaid", "CircumstancesEmploymentChangeWhatDatePaid")
  declareInput("#whatWasIncluded", "CircumstancesEmploymentChangeWhatWasIncluded")
  declareSelect("#howOften_frequency", "CircumstancesEmploymentChangeHowOftenFrequency")
  declareInput("#howOften_frequency_other", "CircumstancesEmploymentChangeHowOftenFrequencyOther")
  declareInput("#monthlyPayDay", "CircumstancesEmploymentChangeMonthlyPayDay")
  declareYesNo("#usuallyPaidSameAmount", "CircumstancesEmploymentChangeUsuallyPaidSameAmount")
  declareYesNo("#employerOwesYouMoney", "CircumstancesEmploymentChangeEmployerOwesYouMoney")
  declareInput("#employerOwesYouMoneyInfo", "CircumstancesEmploymentChangeEmployerOwesYouMoneyInfo")
  declareYesNo("#didYouPayIntoPension_answer", "CircumstancesEmploymentChangeDidYouPayIntoPensionAnswer")
  declareInput("#didYouPayIntoPension_whatFor", "CircumstancesEmploymentChangeDidYouPayIntoPensionWhatFor")
  declareYesNo("#didYouPayForThings_answer", "CircumstancesEmploymentChangeDidYouPayForThingsAnswer")
  declareInput("#didYouPayForThings_whatFor", "CircumstancesEmploymentChangeDidYouPayForThingsWhatFor")
  declareYesNo("#didCareCostsForThisWork_answer", "CircumstancesEmploymentChangeDoCareCostsForThisWorkAnswer")
  declareInput("#didCareCostsForThisWork_whatCosts", "CircumstancesEmploymentChangeDoCareCostsForThisWorkWhatCosts")
  declareInput("#moreAboutChanges", "CircumstancesEmploymentChangeMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G11StartedAndFinishedEmploymentPage {
  val title = "Finished Employment - Change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/employment-finished"

  def apply(ctx:PageObjectsContext) = new G11StartedAndFinishedEmploymentPage(ctx)
}

/** The context for Specs tests */
trait G11StartedAndFinishedEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G11StartedAndFinishedEmploymentPage(PageObjectsContext(browser))
}