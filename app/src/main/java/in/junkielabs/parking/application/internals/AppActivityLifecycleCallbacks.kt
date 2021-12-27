package `in`.junkielabs.parking.application.internals

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.Nullable

class AppActivityLifecycleCallbacks (private val application: Application) {
    private var liveActivityOrNull: Activity? = null
    private var activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null

    init {
        registerActivityLifeCycle()
    }


    private fun registerActivityLifeCycle() {

        //log.w( "activity lifecycle init");
        if (activityLifecycleCallbacks != null) application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks)


        //log.w( "activity lifecycle app callback");
        activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                liveActivityOrNull = activity
                //log.w("activity lifecycle app attached");
            }

            override fun onActivityStarted(activity: Activity) {
                //log.w("activity lifecycle activity started");
            }

            override fun onActivityResumed(activity: Activity) {
                liveActivityOrNull = activity
//                info("activity lifecycle app attached: "+ liveActivityOrNull?.javaClass?.simpleName);
            }

            override fun onActivityPaused(activity: Activity) {
                liveActivityOrNull = null
                //log.w("activity lifecycle app null");
            }

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        }


        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    @Nullable
    fun getLiveActivityOrNull(): Activity? {
        return liveActivityOrNull
    }

    fun isAppOnBackground(): Boolean {

        return getLiveActivityOrNull() == null
    }

    fun getApplication(): Application {
        return application
    }
}