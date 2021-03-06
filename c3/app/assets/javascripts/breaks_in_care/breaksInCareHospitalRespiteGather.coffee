isChecked = (selector)  -> $("##{selector}").prop('checked')
val = (selector, text) -> if text? then $("##{selector}").val(text) else $("##{selector}").val()
S = (selector) -> $("##{selector}")

window.initEvents = (you, dp, youEnded_yes, youEnded_no, dpEnded_yes, dpEnded_no,
  youEndedWrap, youAdmittedWrap, dpEndedWrap, dpAdmittedWrap, medicalProfession, present, past) ->
  if not isChecked(you)
    hideWrapper(youAdmittedWrap)

  if not isChecked(dp)
    hideWrapper(dpAdmittedWrap)

  if not isChecked(youEnded_yes)
    hideWrapper(youEndedWrap)

  if not isChecked(dpEnded_yes)
    hideWrapper(dpEndedWrap)

  if isChecked(you)
    showWrapper(youAdmittedWrap)

  if isChecked(dp)
    showWrapper(dpAdmittedWrap)

  if isChecked(youEnded_yes)
    showWrapper(youEndedWrap)

  if isChecked(dpEnded_yes)
    showWrapper(dpEndedWrap)

  S(you).on "click", ->
    hideWrapper(dpAdmittedWrap)
    showWrapper(youAdmittedWrap)

  S(dp).on "click", ->
    hideWrapper(youAdmittedWrap)
    showWrapper(dpAdmittedWrap)

  S(youEnded_yes).on "click", ->
    updateYourProfessionLabel(youEnded_yes, youEnded_no, medicalProfession, present, past)
    showWrapper(youEndedWrap)

  S(dpEnded_yes).on "click", ->
    showWrapper(dpEndedWrap)

  S(youEnded_no).on "click", ->
    updateYourProfessionLabel(youEnded_yes, youEnded_no, medicalProfession, present, past)
    hideWrapper(youEndedWrap)

  S(dpEnded_no).on "click", ->
    hideWrapper(dpEndedWrap)

showWrapper = (wrapper) ->
  $("#" + wrapper).slideDown(0).attr 'aria-hidden', 'false'

hideWrapper = (wrapper)->
  clearDownStreamInputs(wrapper)
  $("#" + wrapper).slideUp(0).attr 'aria-hidden', 'true'

clearDownStreamInputs = (wrapper)->
  $("#" + wrapper).find("input").each(clearInput)
  $("#" + wrapper).find("textarea").each(clearInput)

# If we want to also clear the validation error when item is hidden ?
# $("#" + wrapper).find(".validation-error").removeClass("validation-error")
# $("#" + wrapper).find(".validation-message").remove()
clearInput = ->
  if( $(this).attr("type") == "radio" )
    $(this).prop('checked', false)
    $(this).parent().removeClass("selected")
  else
    $(this).val("")

updateYourProfessionLabel = (youEnded_yes, youEnded_no, medicalProfession, present, past) ->
  label = S(medicalProfession).prev()
  if isChecked(youEnded_yes)
    if label
      label.text(past).append(label.children())
  else
    if label
      label.text(present).append(label.children())
