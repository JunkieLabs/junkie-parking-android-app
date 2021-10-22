package `in`.junkielabs.parking.application

import `in`.junkielabs.parking.application.internals.AppActivityLifecycleCallbacks
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Niraj on 25-04-2021.
 */

@HiltAndroidApp
class ApplicationMy:  Application() {

    companion object {
        lateinit var instance: ApplicationMy

        fun hasNetwork(): Boolean {
            return instance.checkIfHasNetwork()
        }
    }

    lateinit var appAccount: AppAccount

    //    lateinit var appAccount: AppAccount
    private lateinit var appActivityLifecycleCallbacks: AppActivityLifecycleCallbacks



    override fun onCreate() {
        super.onCreate()
        appAccount = AppAccount()

        appActivityLifecycleCallbacks = AppActivityLifecycleCallbacks(this)
        instance = this
//
//        initTbFont()
//        this.setUpEmoji()
    }
    //

    fun getAppActivityLifecycleCallbacks(): AppActivityLifecycleCallbacks {
        return appActivityLifecycleCallbacks
    }
//

//    private fun initTbFont() {
//        ViewPump.init(
//            ViewPump.builder()
//                .addInterceptor(
//                    CalligraphyInterceptor(
//                        CalligraphyConfig.Builder()
//                            .setDefaultFontPath("fonts/opensans_regular.ttf")
//                            .setFontAttrId(R.attr.fontPath)
//                            .build()
//                    )
//                )
//                .build()
//        )
//    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val capabilities: NetworkCapabilities? =
                cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        }else{
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        return false

    }
}