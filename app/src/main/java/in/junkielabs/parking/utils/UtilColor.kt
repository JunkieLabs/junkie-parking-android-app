package `in`.junkielabs.parking.utils

import `in`.junkielabs.parking.R
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.ColorUtils
import java.util.*

object UtilColor {
    // This default color int
    const val defaultColor = R.color.colorAccent
    //    public static final String TAG = "ColorTransparentUtils";
    /**
     * This method convert numver into hexa number or we can say transparent code
     * @param trans number of transparency you want
     * @return it return hex decimal number or transparency code
     */
    fun convert(trans: Int): String {
        val hexString = Integer.toHexString(Math.round(255 * trans / 100f))
        return (if (hexString.length < 2) "0" else "") + hexString
    }

    fun transparentColor(colorCode: Int, trans: Int): String {
        return convertIntoColor(colorCode, trans)
    }

    /**
     * Convert color code into transparent color code
     * @param colorCode color code
     * @param transCode transparent number
     * @return transparent color code
     */
    fun convertIntoColor(colorCode: Int, transCode: Int): String {
        // convert color code into hexa string and remove starting 2 digit
        val color = Integer.toHexString(colorCode).toUpperCase(Locale.getDefault()).substring(2)
        return if (!color.isEmpty() && transCode > 100) {
            if (color.trim { it <= ' ' }.length == 6) {
                "#" + convert(transCode) + color
            } else {
//                Log.d(TAG,"Color is already with transparency");
                convert(transCode) + color
            }
        } else "#" + Integer.toHexString(defaultColor).toUpperCase(Locale.getDefault())
            .substring(2)
        // if color is empty or any other problem occur then we return deafult color;
    }

    fun getColorForAlpha(colorCode: Int, ratio: Float): Int {
        var newColor = 0
        val alpha = Math.round(Color.alpha(colorCode) * ratio)
        Log.i("Adde: ", "getColorForAlpha ase color sd: $alpha")
        val r = Color.red(colorCode)
        val g = Color.green(colorCode)
        val b = Color.blue(colorCode)
        newColor = Color.argb(alpha, r, g, b)
        return newColor
    }

//    fun rgba2rgb(rgbBackground: Int, rgbaColor: Int)
//    {
//        var alpha = Color.alpha(rgbaColor).toFloat() / 255f
//
//        return Color.rgb(
//                (1f - alpha) * Color.red(rgbBackground).toFloat() + alpha * Color.red(rgbaColor).toFloat(),
//        (1f - alpha) * Color.green(rgbBackground) + alpha * Color.green(rgbaColor),
//        (1f - alpha) * Color.blue(rgbBackground) + alpha * Color.blue(rgbaColor)
//        )
//    }


}