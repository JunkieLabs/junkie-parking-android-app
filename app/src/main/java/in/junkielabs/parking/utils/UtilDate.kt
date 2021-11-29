package `in`.junkielabs.parking.utils

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale





/**
 * Created by Niraj on 29-11-2021.
 */
object UtilDate {


    const val DATE_FORMAT_HOUR = "h:mm aa"

    const val FORMAT_DATE_READABLE = "dd MMM yyyy"

    const val FORMAT_DATE_READABLE2 = "dd/MM h:mm a"

    fun getFormattedDateTime(time: Long, template: String, context: Context): String {
        var locale = getCurrentLocale(context)
        if(locale==null){
            Locale.getDefault()
        }
        val localizedPattern = getLocalizedPattern(template, locale!!)

        return SimpleDateFormat(localizedPattern, locale).format(Date(time))
    }

    fun getLocalizedPattern(template: String, locale: Locale): String? {
        return DateFormat.getBestDateTimePattern(locale, template)
    }

    fun getIsoFormattedDate(context: Context, date: Date): String {

//        info { "getIsoFormattedDate: $date" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = date.toInstant().atZone(ZoneId.systemDefault()) //.toLocalDateTime()
//            info { localDate.toString() }
            val formatter = DateTimeFormatter.ISO_INSTANT
            val text = localDate.format(formatter)
            return text
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", getCurrentLocale(context))
            val formattedDate: String = sdf.format(Date())
            return  formattedDate
        }


    }

    fun getDateFromFormattedString(context: Context, dateString: String): Date {

//        info { "getIsoFormattedDate: $date" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localDate = Instant.parse(dateString) //.atZone(ZoneId.systemDefault()) //.toLocalDateTime()
//            info { localDate.toString() }
//            Instant.now().
            val formatter = DateTimeFormatter.ISO_INSTANT

            val date = Date(localDate.toEpochMilli())
            return date
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", getCurrentLocale(context))
            val date = sdf.parse(dateString)
            return  date
        }


    }

    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0) ?: Locale.getDefault()
        } else {
            context.resources.configuration.locale ?: Locale.getDefault()
        }
    }

    fun convertMillisToTime(timeInMillis: Long): String {
        val minutes = ((timeInMillis / 1000) / 60)
        val seconds = ((timeInMillis / 1000) % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun getHourFromTimeStamp(timestamp: Long): String {
        var date: String = ""
        val dateFormat = DATE_FORMAT_HOUR
        try {
            date = DateFormat.format(dateFormat, timestamp).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return date
    }

}