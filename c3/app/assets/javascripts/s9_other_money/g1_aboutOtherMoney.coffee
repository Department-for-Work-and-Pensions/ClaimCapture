window.initEvents = (answerY, answerN, whoPaysYou, howMuch, howOften_frequency, howOften_frequency_other,
                     statSickPayY, statSickPayN,
                     statHowMuch, statHowOften_frequency, statHowOften_frequency_other, statEmployersName,
                     statEmployersAddress_lineOne, statEmployersAddress_lineTwo, statEmployersAddress_lineThree, statEmployersPostcode
                     otherPayY, otherPayN,
                     otherHowMuch, otherHowOften_frequency, otherHowOften_frequency_other, otherEmployersName,
                     otherEmployersAddress_lineOne, otherEmployersAddress_lineTwo, otherEmployersAddress_lineThree, otherEmployersPostcode) ->
  $("#" + answerY).on "click", ->
    $("#otherPayWrap").slideDown()
    $("#otherPayWrap").css('display', "block")

  $("#" + answerN).on "click", ->
    $("#otherPayWrap").slideUp( ->
      $("#"+whoPaysYou).val("")
      $("#"+howMuch).val("")
      $("#"+howOften_frequency).val("")
      $("#"+howOften_frequency_other).val(""))

  $("#" + statSickPayY).on "click", ->
    $("#sickPayWrap").slideDown 500
    $("#sickPayWrap").css('display', "block")

  $("#" + statSickPayN).on "click", ->
    $("#sickPayWrap").slideUp ->
      $("#"+statHowMuch).val("")
      $("#"+statHowOften_frequency).val("")
      $("#"+statHowOften_frequency_other).val("")
      $("#"+statEmployersName).val("")
      $("#"+statEmployersAddress_lineOne).val("")
      $("#"+statEmployersAddress_lineTwo).val("")
      $("#"+statEmployersAddress_lineThree).val("")
      $("#"+statEmployersPostcode).val("")

  $("#" + otherPayY).on "click", ->
    $("#otherStatPayWrap").slideDown()
    $("#otherStatPayWrap").css('display', "block")

  $("#" + otherPayN).on "click", ->
    $("#otherStatPayWrap").slideUp ->
      $("#"+otherHowMuch).val("")
      $("#"+otherHowOften_frequency).val("")
      $("#"+otherHowOften_frequency_other).val("")
      $("#"+otherEmployersName).val("")
      $("#"+otherEmployersAddress_lineOne).val("")
      $("#"+otherEmployersAddress_lineTwo).val("")
      $("#"+otherEmployersAddress_lineThree).val("")
      $("#"+otherEmployersPostcode).val("")
