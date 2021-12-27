package `in`.junkielabs.parking.utils

import `in`.junkielabs.parking.R
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


/**
 * Created by niraj on 21-04-2021.
 */
object UtilTheme {

    @JvmStatic fun setDefaultStatusBar(view: View, activity: Activity, @ColorInt color: Int) {
        val window: Window = activity.getWindow()


        var mode = activity.getResources().getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wic = view.windowInsetsController

           if(mode == Configuration.UI_MODE_NIGHT_YES){
               wic?.setSystemBarsAppearance(
                   0,
                   APPEARANCE_LIGHT_STATUS_BARS
               )
           }else{
               wic?.setSystemBarsAppearance(
                   APPEARANCE_LIGHT_STATUS_BARS,
                   APPEARANCE_LIGHT_STATUS_BARS
               )
           }
//            wic?.setSystemBarsAppearance(
//                APPEARANCE_LIGHT_STATUS_BARS,
//                APPEARANCE_LIGHT_STATUS_BARS
//            )
            window.setStatusBarColor(color)
            return
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility

            if(mode == Configuration.UI_MODE_NIGHT_NO){
                flags  = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            }
            view.systemUiVisibility = flags
            window.setStatusBarColor(color)
            return
        }


/*        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && a.isColorType) {
            // windowBackground is a color
            a.data
        } else {
            // windowBackground is not a color, probably a drawable
            ContextCompat.getColor(activity.applicationContext, R.color.white)
        }*/

// set any light background color to the status bar. e.g. - white or light blue

// set any light background color to the status bar. e.g. - white or light blue

    }

    @JvmStatic
    fun setDarkStatusBar(view: View, activity: Activity, @ColorInt color: Int) {
        val mode =
            activity.getResources().getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK

        val window: Window = activity.getWindow()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            val wic = view.windowInsetsController
//            wic?.setSystemBarsAppearance(
//                0,
//                APPEARANCE_LIGHT_STATUS_BARS
//            )
            if (mode == Configuration.UI_MODE_NIGHT_YES) {

                wic?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                wic?.setSystemBarsAppearance(
                    0,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            }

//            wic?.setSystemBarsAppearance(
//                APPEARANCE_LIGHT_STATUS_BARS,
//                APPEARANCE_LIGHT_STATUS_BARS
//            )
            window.setStatusBarColor(color)
            return
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility


            if (mode == Configuration.UI_MODE_NIGHT_YES) {

                flags = flags or (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {

                flags = flags.minus(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }
            view.systemUiVisibility = flags
            window.setStatusBarColor(color)
        }

        return
    }

    @JvmStatic fun setAccentStatusBar(view: View, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
//            flags = flags or View.SYSTEM_UI_FLAG_
            view.systemUiVisibility = flags
            activity.window.statusBarColor = ContextCompat.getColor(
                activity.applicationContext,
                R.color.colorAccent
            )
        }
    }

    @JvmStatic fun setStatusBar(view: View, activity: Activity, color: Int, isDefault: Boolean) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            var flags = view.systemUiVisibility
//            flags = flags or View.SYSTEM_UI_FLAG_
            if(isDefault){

                setDefaultStatusBar(view, activity, color)
            }else{

                activity.window.statusBarColor = color
            }

//        }
    }
}