package utils.pageobjects.s4_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

final class G10BreaksInCarePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G10BreaksInCarePage.url, iteration) {
   declareYesNo("#answer", "AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G10BreaksInCarePage {
  val url  = "/care-you-provide/breaks-in-care"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G10BreaksInCarePage(ctx,iteration)
}

/** The context for Specs tests */
trait G10BreaksInCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10BreaksInCarePage apply(PageObjectsContext(browser), iteration = 1)
}