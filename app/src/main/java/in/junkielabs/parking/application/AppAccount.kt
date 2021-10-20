package `in`.junkielabs.parking.application

import `in`.junkielabs.parking.components.api.models.guard.ParamGuard

/**
 * Created by Niraj on 20-10-2021.
 */
class AppAccount {


    private var mGuard: ParamGuard? = null

    private var accountChecked: Boolean = false

    fun hasAccount(): Boolean {
        return mGuard!=null
    }

    fun reset() {
        mGuard = null
    }

    fun getGuard(): ParamGuard? {
        return mGuard
    }

    fun getGuardId(): String?{
        return mGuard?.id
    }

    fun setGuard(guard: ParamGuard): Boolean {
//        info { "Guard: $guard" }
        mGuard = guard
        return true
    }

}