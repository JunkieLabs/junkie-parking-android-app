package `in`.junkielabs.parking.ui.components.launcher.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoGuard
import `in`.junkielabs.parking.components.api.repository.ApiRepoParkingArea
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import `in`.junkielabs.parking.tools.livedata.LiveDataEvent
import `in`.junkielabs.parking.ui.components.launcher.ActivityLauncher
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Niraj on 20-10-2021.
 */
class LauncherViewModel(
    application: Application,
    var apiRepoAuth: ApiRepoAuth,
    var apiRepoGuard: ApiRepoGuard,
    var apiRepoParkingArea: ApiRepoParkingArea
) : AndroidViewModel(application) {

    private val _mEventAccountState = MutableLiveData<LiveDataEvent<Int>>()
    val mEventAccountState: LiveData<LiveDataEvent<Int>> = _mEventAccountState

    private val _mEventExit = MutableLiveData<LiveDataEvent<Boolean>>()
    val mEventExit: LiveData<LiveDataEvent<Boolean>> = _mEventExit

    var mShouldRemoveAccount = false

    override fun onCleared() {
        super.onCleared()
    }

    fun checkHasAccount() {

        viewModelScope.launch {
            var accountState = getAccountState()
            Log.i("LauncherViewModel", "checkHasAccount account state")


            if (accountState == AccountConstants.AccountUser.STATE_AUTHORIZED || accountState == AccountConstants.AccountUser.STATE_WAITING) {
                Log.i(
                    "LauncherViewModel",
                    "Enrollment info called for account state ${AccountConstants.AccountUser.STATE_AUTHORIZED}"
                )

                var isSuccess = apiGetGuardInfo()
                if(isSuccess){
                    isSuccess = apiGetParkingArea()
                }
                if(!isSuccess){
                    _mEventExit.postValue(LiveDataEvent(true));
                }else{
                    accountState = getAccountState()
                    _mEventAccountState.postValue(LiveDataEvent(accountState))
                }


            }else{
                _mEventAccountState.postValue(LiveDataEvent(accountState))
            }


//            (application as ApplicationMy).appAccount.setHasAccount(hasAccount)

        }


    }

    private suspend fun getAccountState() = suspendCoroutine<Int> {

        var currentUser = FirebaseAuth.getInstance().currentUser
//        var parkingAreaId = AccountPreference.getInstance().getParkingAreaId()
//        var guardId = AccountPreference.getInstance().getGuardId()
        var guardId = getApplication<ApplicationMy>().appAccount.getGuardId()
        var parkingAreaId = getApplication<ApplicationMy>().appAccount.getParkingAreaId()

        if (parkingAreaId != null) {
            it.resume(AccountConstants.AccountUser.STATE_AUTHORIZED)
        } else if (guardId != null) {
            it.resume(AccountConstants.AccountUser.STATE_WAITING)

        } else if (currentUser != null) {
            it.resume(AccountConstants.AccountUser.STATE_REAUTH)

        } else {
            it.resume(AccountConstants.AccountUser.STATE_NOT_EXIST)

        }

    }

    fun onSignInResult() {
        viewModelScope.launch {
            var accountState = getAccountState()
            if (accountState == AccountConstants.AccountUser.STATE_AUTHORIZED || accountState == AccountConstants.AccountUser.STATE_WAITING) {
                _mEventAccountState.postValue(LiveDataEvent(accountState))

            }



        }
    }


/*
    fun reqReAuth(activity: ActivityLauncher) {
        val options = Bundle()
        options.putString(
            AccountConstants.Account.Arguments.ACCOUNT_ACTION,
            AccountConstants.Account.ACTION_REAUTH
        )
        mAppAccountManager.requestAddAccount(activity, options, accountCallback())
    }
*/

    /* ************************************************************************************
     *                                      api
     */

    private suspend fun apiGetGuardInfo(): Boolean {
        var guardId = getApplication<ApplicationMy>().appAccount.getGuardId()

        var token = FirebaseToken.getToken()
        if (guardId != null && token != null) {
            var response = apiRepoGuard.getById(token, guardId);
            if (response.status == ApiResponse.Status.SUCCESS && response.data != null) {

                Log.i("apiGetGuardInfo", "${response.data}");
                getApplication<ApplicationMy>().appAccount.setGuard(response.data!!)
                return true
            }else {
                if(response.errorData!=null){
                   if(response.errorData!!.code ==  HttpURLConnection.HTTP_NOT_FOUND){
                       mShouldRemoveAccount = true
                   }
                }
                //response.errorData
            }
        }
        return false
    }

    private suspend fun apiGetParkingArea(): Boolean {
        var parkingAreaId = getApplication<ApplicationMy>().appAccount.getParkingAreaId()

        var token = FirebaseToken.getToken()
        if (parkingAreaId != null && token != null) {
            var response = apiRepoParkingArea.getById(token, parkingAreaId);
            if (response.status == ApiResponse.Status.SUCCESS && response.data != null) {

                Log.i("apiGetParkingArea", "${response.data}");
                getApplication<ApplicationMy>().appAccount.setParkingArea(response.data!!)
                return true
            }
        }

        return false
    }


}