package `in`.junkielabs.parking.utils

import android.util.Log

/**
 * Created by Niraj on 13-11-2021.
 */
object UtilRegex {
    fun getInputBoxes(inputFormat: String, input: String): List<String> {
        var regex = Regex("([^\\s|\\-|/]{1,}|[\\s|\\-|/]{1,})")
        var regexSymbol = Regex("[\\s|\\-|/]{1,}")

        var matched = regex.findAll(inputFormat);
        var formatList = matched.map { matchResult -> matchResult.value }.toList()
        var inputList = regex.findAll(input).map { matchResult -> matchResult.value }.toList();
//        Log.d("UtilRegex:", "getInputBoxes: $formatList $inputList")

        var result = mutableListOf<String>()

        var formatIndex = 0;

        // TODO check if input length exceed
        // TODO while loop until text is formatted
        for (i in inputList.indices) {

            if (formatIndex < formatList.size) {

                var formatText = formatList[formatIndex]
                var inputTxt = inputList[i]

                var inputIsSymbol = regexSymbol.matches(inputTxt)
                var formatIsSymbol = regexSymbol.matches(formatText)
//                Log.d("UtilRegex:", "getInputBoxes 1 : $i $inputIsSymbol $formatIsSymbol")

                if (inputIsSymbol == formatIsSymbol && inputIsSymbol) {
                    result.add(formatText)
                    formatIndex++;
//                        Log.d("UtilRegex:", "getInputBoxes 1 : $i $inputIsSymbol $formatIsSymbol")
                } else if (inputIsSymbol == formatIsSymbol && !inputIsSymbol && inputTxt.length == formatText.length) {
                    result.add(inputTxt)
                    formatIndex++;
                } else if (inputIsSymbol) {
                    formatIndex++;
                } else {
                    var expText = inputTxt;


                    while (formatIndex < formatList.size && expText.isNotEmpty()) {
                        formatText = formatList[formatIndex]
                        formatIsSymbol = regexSymbol.matches(formatText)

                        if (formatIsSymbol) {
                            result.add(formatText)


                        } else {

                            if (expText.length > formatText.length) {
                                result.add(expText.substring(0, formatText.length))
                                expText = expText.substring(formatText.length, expText.length)
                            } else {
                                result.add(expText)
                                expText = ""
                            }

                        }
                        formatIndex++
                    }


                }


            }


        }

        return result;
    }
}