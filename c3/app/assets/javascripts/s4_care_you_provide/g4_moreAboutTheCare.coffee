window.initEvents = (spent35HoursCaringBeforeClaimY, spent35HoursCaringBeforeClaimN, day, month, year) ->

  if not $("#" + spent35HoursCaringBeforeClaimY).prop('checked')
    hideCareStartDateWrap(day, month, year)

  $("#" + spent35HoursCaringBeforeClaimY).on "click", ->
    showCareStartDateWrap()

  $("#" + spent35HoursCaringBeforeClaimN).on "click", ->
    hideCareStartDateWrap(day, month, year)


showCareStartDateWrap = ->
  $("#careStartDateWrap").slideDown 0

hideCareStartDateWrap = (day, month, year) ->
    $("#careStartDateWrap").slideUp 0
    $("#" + day).val("")
    $("#" + month).val("")
    $("#" + year).val("")