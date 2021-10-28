package `in`.junkielabs.parking.application

import `in`.junkielabs.parking.components.account.AccountPreference
import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import `in`.junkielabs.parking.components.api.models.parking.area.ParamParkingArea

/**
 * Created by Niraj on 20-10-2021.
 */
class AppAccount {


    private var mParkingArea: ParamParkingArea? = null
    private var mGuard: ParamGuard? = null

    private var mParkingAreaId: String? = null
    private var mGuardId: String? = null


    private var isPreferenceLoaded = false

    fun hasAccount(): Boolean {
        return mGuard!=null
    }

    fun reset() {
        AccountPreference.getInstance().clearPrefs()
        mGuard = null
        mGuardId = null
        mParkingAreaId = null
    }

    fun getGuard(): ParamGuard? {

        return mGuard
    }



    fun getGuardId(): String?{

        if(!isPreferenceLoaded){
            loadPreference()
        }
        return mGuardId
    }

    fun setGuard(guard: ParamGuard): Boolean {
//        info { "Guard: $guard" }
        mGuard = guard
        mParkingAreaId = guard.parkingAreaId

        AccountPreference.getInstance().setGuardId(mGuard?.id)
        AccountPreference.getInstance().setParkingAccountId(mGuard?.parkingAccountId)
        AccountPreference.getInstance().setParkingAreaId(mGuard?.parkingAreaId)
        loadPreference()
        return true
    }

    fun setParkingArea(parkingArea: ParamParkingArea?) {
        mParkingArea = parkingArea
    }

    fun getParkingArea(): ParamParkingArea? {
        return mParkingArea
    }

    fun getParkingAreaId(): String? {

        if(!isPreferenceLoaded){
            loadPreference()
        }
        return mParkingAreaId
    }

    private fun loadPreference(){
       mGuardId =  AccountPreference.getInstance().getGuardId()
        mParkingAreaId =  AccountPreference.getInstance().getParkingAreaId()
        isPreferenceLoaded = true

    }

}