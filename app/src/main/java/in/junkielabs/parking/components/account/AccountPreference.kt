package `in`.junkielabs.parking.components.account

import `in`.junkielabs.parking.application.ApplicationMy
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Created by Niraj on 20-10-2021.
 */
class AccountPreference {

    companion object {

        private const val STORAGE_KEY = "pref.user"

        private const val PARKING_ACCOUNT_ID = "parkingAccountId"
        private const val PARKING_AREA_ID = "parkingAreaId"
        private const val GUARD_ID = "guardId"

        private var sAccountPreference: AccountPreference? = null

        private val lock = Any()


        fun getInstance(
        ): AccountPreference {
            synchronized(lock) {
                if (sAccountPreference == null) {
                    sAccountPreference = AccountPreference()


                    /*   return Observable.fromCallable(new Callable<AppNotifications>() {
                    @Override
                    public AppNotifications call() throws Exception {
                        appNotifications = new AppNotifications(context);
                        appNotifications.initialize();
                        return appNotifications;
                    }
                }).subscribeOn(Schedulers.io());*/
                }
                return sAccountPreference!!
            }
        }

    }


    private fun getSharedPreferences(): SharedPreferences {
//       context: Context
        var context = ApplicationMy.instance

        return context.getSharedPreferences(STORAGE_KEY, 0)
    }





    fun setParkingAccountId(  id: String) {
        val sp = getSharedPreferences()
        sp.edit()
            .putString(PARKING_ACCOUNT_ID, id)
            .apply()
    }


    fun getParkingAccountId(): String? {
        val sp = getSharedPreferences()
        if (sp.contains(PARKING_ACCOUNT_ID)) {
            return sp.getString(PARKING_ACCOUNT_ID, null);
        }
        return null
    }


    fun setParkingAreaId(id: String) {
        val sp = getSharedPreferences()
        sp.edit()
            .putString(PARKING_AREA_ID, id)
            .apply()
    }


    fun getParkingAreaId(): String? {
        val sp = getSharedPreferences()
        if (sp.contains(PARKING_AREA_ID)) {
            return sp.getString(PARKING_AREA_ID, null);
        }
        return null
    }


    fun setGuardId(id: String) {
        val sp = getSharedPreferences()
        sp.edit()
            .putString(GUARD_ID, id)
            .apply()
    }


    fun getGuardId(): String? {
        val sp = getSharedPreferences()
        if (sp.contains(GUARD_ID)) {
            return sp.getString(GUARD_ID, null);
        }
        return null
    }

    fun printAll(): Boolean {

        val sp = getSharedPreferences()
        val keys = sp.all

        for (entry in keys.entries) {
            Log.i("AccountPreference",
                "AccountPreference: map values: " + entry.key + ": " +
                        entry.value.toString()
            )
        }
        return true
    }


    fun clearPrefs() {
        getSharedPreferences().edit().clear().apply()
    }
}