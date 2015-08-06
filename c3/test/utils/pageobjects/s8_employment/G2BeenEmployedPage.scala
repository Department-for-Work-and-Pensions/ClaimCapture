package utils.pageobjects.s8_employment

import utils.WithBrowser
import utils.pageobjects._

final class G2BeenEmployedPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G2BeenEmployedPage.url, iteration) {
  declareYesNo("#beenEmployed", "EmploymentHaveYouBeenEmployedAtAnyTime_"+iteration)
  override def getNewIterationNumber: Int = {
    import IterationManager._
    ctx.iterationManager.increment(Employment)
  }
}

object G2BeenEmployedPage {
  val url  = "/employment/been-employed"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G2BeenEmployedPage(ctx,iteration)
}

trait G2BeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2BeenEmployedPage (PageObjectsContext(browser),iteration=1)
}