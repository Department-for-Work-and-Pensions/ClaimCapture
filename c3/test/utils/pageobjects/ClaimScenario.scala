package utils.pageobjects

import scala.language.dynamics
/**
 * Represents a full claim. Fill only the responses you need for the your specific test scenario.
 * The class is dynamic. It builds of attributes dynamically. To create a new attribute and provide a value, just
 * write:<br>
 *  <i>val claim = new ClaimScenario</i><br>
 *  <i>claim.[A New Name Of Attribute] =  [value]</i><br>
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class ClaimScenario extends Dynamic {

  var map = Map.empty[String,String]

  def selectDynamic(name: String) =
    map get name getOrElse sys.error("method not found")

  def updateDynamic(name: String)(value: String) {
    map += name -> value
  }

//  var `About You - Have you sublet your home`: String = null
//  var `About You - What is your visa reference number`: String = null
//  var `About You - Address`: String = null
//  var `About You - All other surnames or family names`: String = null
//  var `About You - Are you currently living in the UK?`: String = null
//  var `About You - Date of birth`: String = null
//  var `About You - Daytime phone number`: String = null
//  var `About You - Do you get state Pension?`: String = null
//  var `About You - Do you or your partner/spouse own property or land?`: String = null
//  var `About You - Do you plan to go back to that country?`: String = null
//  var `About You - First name`: String = null
//  var `About You - Have you always lived in the UK?`: String = null
//  var `About You - Have you been employed at any time?`: String = null
//  var `About You - Have you been on a course of education ? `: String = null
//  var `About You - Have you been self-employed at any time?`: String = null
//  var `About You - Have you had a partner/spouse at any time?`: String = null
//  var `About You - Have you or your partner/spouse claimed or received any other benefits?`: String = null
//  var `About You - Middle name`: String = null
//  var `About You - Mobile number`: String = null
//  var `About You - National Insurance (NI) number`: String = null
//  var `About You - Nationality`: String = null
//  var `About You - Postcode`: String = null
//  var `About You - Surname or family name`: String = null
//  var `About You - Title`: String = null
//  var `About You - What country did you come from?`: String = null
//  var `About You - What is your marital or civil partnership status?`: String = null
//  var `About You - When did you arrive in the UK?`: String = null
//  var `About You - When do you plan to go back?`: String = null
//  var `About You - When do you want your Carer's Allowance claim to start?`: String = null
//  var `About Your Partner - Nationality`: String = null
//  var `About Your Partner - Address`: String = null
//  var `About Your Partner - All other surnames or family names`: String = null
//  var `About Your Partner - Date of birth`: String = null
//  var `About Your Partner - Does your partner/spouse live at the same address as you?`: String = null
//  var `About Your Partner - First name`: String = null
//  var `About Your Partner - Have you separated from your partner/spouse?`: String = null
//  var `About Your Partner - Is your partner/spouse the person you are claiming Carer's Allowance for?`: String = null
//  var `About Your Partner - Middle name`: String = null
//  var `About Your Partner - National Insurance (NI) number`: String = null
//  var `About Your Partner - The date when you and your partner/spouse started living together?`: String = null
//  var `About Your Partner - Postcode`: String = null
//  var `About Your Partner - Surname or family name`: String = null
//  var `About Your Partner - Title`: String = null
//  var `About Your Partner - When did you separate?`: String = null
//  var `About the care you provide - Have you had any breaks in caring since claim - 6 months?`: String = null
//  var `About the care you provide - Address person care for`: String = null
//  var `About the care you provide - Address person pays you`: String = null
//  var `About the care you provide - Address previous carer`: String = null
//  var `About the care you provide - Break end date?`: String = null
//  var `About the care you provide - Break end time?`: String = null
//  var `About the care you provide - Break start date?`: String = null
//  var `About the care you provide - Break start time?`: String = null
//  var `About the care you provide - Date of birth person you care`: String = null
//  var `About the care you provide - Date of birth previous carer`: String = null
//  var `About the care you provide - Daytime phone number person you care`: String = null
//  var `About the care you provide - Daytime phone number previous carer`: String = null
//  var `About the care you provide - Did you care for this person for 35 hours?`: String = null
//  var `About the care you provide - Did you or the person you care for get any medical treatment during the break?`: String = null
//  var `About the care you provide - Do they live at the same address as you?`: String = null
//  var `About the care you provide - Do you act for the person you care for?`: String = null
//  var `About the care you provide - Do you spend 35 hours or more each week?`: String = null
//  var `About the care you provide - Does someone else act for the person you care for?`: String = null
//  var `About the care you provide - Does this person get Armed Forces Independence Payment?`: String = null
//  var `About the care you provide - First name person care for`: String = null
//  var `About the care you provide - First name person pays you`: String = null
//  var `About the care you provide - First name previous carer`: String = null
//  var `About the care you provide - Full name representatives person you care for`: String = null
//  var `About the care you provide - Has anyone else claimed Carer Allowance?`: String = null
//  var `About the care you provide - Has someone paid you to care?`: String = null
//  var `About the care you provide - Have you had any more breaks in care?  `: String = null
//  var `About the care you provide - How much do you get paid a week?`: String = null
//  var `About the care you provide - Middle name person care for`: String = null
//  var `About the care you provide - Middle name person pays you`: String = null
//  var `About the care you provide - Middle name previous carer`: String = null
//  var `About the care you provide - Mobile number previous carer`: String = null
//  var `About the care you provide - National Insurance (NI) number person care for`: String = null
//  var `About the care you provide - National Insurance (NI) number previous carer`: String = null
//  var `About the care you provide - Organisation (optional)`: String = null
//  var `About the care you provide - Person acts as`: String = null
//  var `About the care you provide - Postcode person care for`: String = null
//  var `About the care you provide - Postcode person pays you`: String = null
//  var `About the care you provide - Postcode previous carer`: String = null
//  var `About the care you provide - Surname or family name person care for`: String = null
//  var `About the care you provide - Surname or family name person pays you`: String = null
//  var `About the care you provide - Surname or family name previous carer`: String = null
//  var `About the care you provide - Title person care for`: String = null
//  var `About the care you provide - Title person pays you`: String = null
//  var `About the care you provide - What's their relationship to you?`: String = null
//  var `About the care you provide - When did the payments start?`: String = null
//  var `About the care you provide - When did you start to care for this person?`: String = null
//  var `About the care you provide - Where was the person you care for during the break? `: String = null
//  var `About the care you provide - Where were you during the break? `: String = null
//  var `About the care you provide - You act as`: String = null
//  var `Can you get Carers Allowance? - Are you aged 16 or over?`: String = null
//  var `Can you get Carers Allowance? - Do you normally live in GB?`: String = null
//  var `Can you get Carers Allowance? - Do you spend 35 hours or more each week caring?`: String = null
//  var `Can you get Carers Allowance? - Does the person you care for get one of these benefits?`: String = null
//  var `Consent & Declaration - Disclaimer text and tick box`: String = null
//  var `Consent & Declaration - Getting information from any current or previous employer?`: String = null
//  var `Consent & Declaration - Getting information from any other person or organisation?`: String = null
//  var `Consent & Declaration - Tell us anything else you think we should know about your claim.`: String = null
//  var `Consent & Declaration - Tell us why`: String = null
//  var `Education - Name of school, college, university`: String = null
//  var `Education - Address`: String = null
//  var `Education - Course title`: String = null
//  var `Education - Fax Number`: String = null
//  var `Education - Name of Main Teacher or Tutor`: String = null
//  var `Education - Phone number`: String = null
//  var `Education - Post code`: String = null
//  var `Education - Type of course`: String = null
//  var `Education - when did you finish`: String = null
//  var `Education - When did you start the course?`: String = null
//  var `Education - When do you expect the course to end?`: String = null
//  var `Education - Your student reference number`: String = null
//  var `Employment - Address care provider`: String = null
//  var `Employment - Address childcare provider`: String = null
//  var `Employment - Address employer`: String = null
//  var `Employment - Do you pay anyone else to look after your child while at work?`: String = null
//  var `Employment - Does your employer owe you any money?`: String = null
//  var `Employment - Employer Name`: String = null
//  var `Employment - Have you finished this job?`: String = null
//  var `Employment - Have you had another job at any time since claim - 6 months?`: String = null
//  var `Employment - How Often?`: String = null
//  var `Employment - How many hours a week you normally work?`: String = null
//  var `Employment - Childcare expenses - How much you pay for?`: String = null
//  var `Employment - Care expenses - How much you pay for?`: String = null
//  var `Employment - How much you pay for organisation pension?`: String = null
//  var `Employment - How much you pay for private pension?`: String = null
//  var `Employment - How much are you owed?`: String = null
//  var `Employment - How much did these things cost you each week?`: String = null
//  var `Employment - Additional wages - How often were you paid?`: String = null
//  var `Employment - How often organisation pension?`: String = null
//  var `Employment - How often private pension?`: String = null
//  var `Employment - Is this the same as the other job you have told us about?`: String = null
//  var `Employment - Job title`: String = null
//  var `Employment - Name of the organisation who looks after your child`: String = null
//  var `Employment - Name of the organisation you pay`: String = null
//  var `Employment - Additional wages - Other`: String = null
//  var `Employment - Payroll or Employee number`: String = null
//  var `Employment - Employer Phone number`: String = null
//  var `Employment - Please tell us what other money you received `: String = null
//  var `Employment - Postcode care provider`: String = null
//  var `Employment - Postcode childcare provider`: String = null
//  var `Employment - Postcode employer`: String = null
//  var `Employment - What are necessary job expenses?`: String = null
//  var `Employment - What is the money owed for?`: String = null
//  var `Employment - What period did this cover?  From`: String = null
//  var `Employment - What period did this cover? To`: String = null
//  var `Employment - What period is it for? From`: String = null
//  var `Employment - What period is it for? To`: String = null
//  var `Employment - What was included in your last pay?`: String = null
//  var `Employment - What was the gross pay for the last pay period?`: String = null
//  var `Employment - What is the leaving date on your P45`: String = null
//  var `Employment - Care expenses - What relation is the person to you `: String = null
//  var `Employment - Childcare expenses - What relation is the person to you `: String = null
//  var `Employment - Childcare expenses - What relation is the person to your partner`: String = null
//  var `Employment - Care expenses - What relation is the person to the person you care for`: String = null
//  var `Employment - Childcare expenses - What relation is the person to the person you care for`: String = null
//  var `Employment - While at work do you pay anyone to look after person you care for`: String = null
//  var `Employment - While at work do you pay anyone to look after your child`: String = null
//  var `Employment - Additional wages - When you get paid?`: String = null
//  var `Employment - When did you last work? `: String = null
//  var `Employment - When did you start your job?`: String = null
//  var `Employment - When should the money owed have been paid?`: String = null
//  var `Employment - When were you last paid?`: String = null
//  var `Employment - When will you get money owed?`: String = null
//  var `Employment - Why you need these expenses to do your job?`: String = null
//  var `Employment - Do you get holiday pay or sick pay?`: String = null
//  var `Employment - Do you get paid any other money as well as your normal wage?`: String = null
//  var `Employment - Do you get the same amount each time?`: String = null
//  var `Employment - Do you pay for anything else necessary to do your job?`: String = null
//  var `Employment - Do you pay for anything necessary to do your job?`: String = null
//  var `Employment - Do you pay towards a scheme or a retirement annuity scheme?`: String = null
//  var `Employment - Do you pay towards an occupational pension scheme?`: String = null
//  var `How we pay you - Account number`: String = null
//  var `How we pay you - Bank Identification Code (BIC)`: String = null
//  var `How we pay you - Building society roll or reference number`: String = null
//  var `How we pay you - Full name of bank or building society `: String = null
//  var `How we pay you - Full name of bank`: String = null
//  var `How we pay you - How often do you want to get paid?`: String = null
//  var `How we pay you - How would you like to get paid?`: String = null
//  var `How we pay you - International Bank Account Number (IBAN)`: String = null
//  var `How we pay you - Name of account holder`: String = null
//  var `How we pay you - Sort code`: String = null
//  var `Self Employed - Accountant Name`: String = null
//  var `Self Employed - Accountant Address`: String = null
//  var `Self Employed - Childcare provider Address`: String = null
//  var `Self Employed - Care provider Address`: String = null
//  var `Self Employed - Are the income/outgoing similar to your current`: String = null
//  var `Self Employed - Are these accounts prepared on a cash flow basis?`: String = null
//  var `Self Employed - Are you self employed now?`: String = null
//  var `Self Employed - Can we contact your accountant?`: String = null
//  var `Self Employed - Do you have an accountant?`: String = null
//  var `Self Employed - Accountant Fax Number`: String = null
//  var `Self Employed - Have you ceased trading?`: String = null
//  var `Self Employed - How much you pay childcare expenses?`: String = null
//  var `Self Employed - How much you pay expenses person you care?`: String = null
//  var `Self Employed - How much you pay for pension/expenses?`: String = null
//  var `Self Employed - How often pay pension/expenses?`: String = null
//  var `Self Employed - Is this the same as the expenses during your employed work?`: String = null
//  var `Self Employed - Name of the organisation who looks after your child`: String = null
//  var `Self Employed - Name of the organisation you pay`: String = null
//  var `Self Employed - Nature of your business`: String = null
//  var `Self Employed - Please tell us why and when the change happened`: String = null
//  var `Self Employed - Accountant Postcode`: String = null
//  var `Self Employed - Childcare provider Postcode`: String = null
//  var `Self Employed - Care provider Postcode`: String = null
//  var `Self Employed - Accountant telephone Number`: String = null
//  var `Self Employed - What was/is your trading year. From`: String = null
//  var `Self Employed - What was/is your trading year. Is`: String = null
//  var `Self Employed - When did the job finish?`: String = null
//  var `Self Employed - When did you start this job?`: String = null
//  var `Self Employed - Do you pay towards a pension scheme?`: String = null
//  var `Self Employed - Care expenses - What relation is the person to you `: String = null
//  var `Self Employed - Childcare expenses - What relation is the person to you `: String = null
//  var `Self Employed - Childcare expenses - What relation is the person to your partner`: String = null
//  var `Self Employed - Care expenses - What relation is the person to the person you care for`: String = null
//  var `Self Employed - Childcare expenses - What relation is the person to the person you care for`: String = null
//  var `Self Employed - While at work do you pay anyone to look after person you care for`: String = null
//  var `Self Employed - While at work do you pay anyone to look after your child`: String = null
//  var `Time Spent Abroad - Do you normally live in the UK`: String = null
//  var `Time Spent Abroad - Have you been out of GB for more than 52 weeks`: String = null
//  var `Time Spent Abroad - Have you been out of GB with the person you care for`: String = null
//  var `Time Spent Abroad - More trips out of GB for more than 52 weeks at a time`: String = null
//  var `Time Spent Abroad - More trips out of GB with the person you care for`: String = null
//  var `Time Spent Abroad - Are you in GB now?`: String = null
//  var `Time Spent Abroad - Date you left GB`: String = null
//  var `Time Spent Abroad - Date you returned to GB`: String = null
//  var `Time Spent Abroad - Where did you go for more than 52 weeks?`: String = null
//  var `Time Spent Abroad - Where did you go with person care for?`: String = null
//  var `Time Spent Abroad - Where do you normally live? `: String = null
//  var `Time Spent Abroad - Why did you go for more than 52 weeks?`: String = null
//  var `Time Spent Abroad - Why did you go with person care for?`: String = null


}
