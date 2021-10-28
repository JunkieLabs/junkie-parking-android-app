package `in`.junkielabs.parking.ui.components.onboard.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoGuard
import `in`.junkielabs.parking.components.api.repository.ApiRepoParkingArea
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import `in`.junkielabs.parking.tools.livedata.LiveDataEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Niraj on 22-10-2021.
 */
class OnboardViewModel(
    application: Application,
    var apiRepoAuth: ApiRepoAuth,
    var apiRepoGuard: ApiRepoGuard,
    var apiRepoParkingArea: ApiRepoParkingArea
) : AndroidViewModel(application) {

    private val _mEventAccountState = MutableLiveData<LiveDataEvent<Int>>()
    val mEventAccountState: LiveData<LiveDataEvent<Int>> = _mEventAccountState

    override fun onCleared() {
        super.onCleared()
    }

    fun signIn() {
        _mEventAccountState.postValue(LiveDataEvent(AccountConstants.AccountUser.STATE_NOT_EXIST))
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
                apiGetParkingArea()
                _mEventAccountState.postValue(LiveDataEvent(accountState))

            }



        }
    }

    /* ****************************************************************************
     *                                  Api
     */

    private suspend fun apiGetParkingArea() {
        var parkingAreaId = getApplication<ApplicationMy>().appAccount.getParkingAreaId()

        var token = FirebaseToken.getToken()
        if (parkingAreaId != null && token != null) {
            var response = apiRepoParkingArea.getById(token, parkingAreaId);
            if (response.status == ApiResponse.Status.SUCCESS && response.data != null) {

                Log.i("apiGetParkingArea", "${response.data}");
                getApplication<ApplicationMy>().appAccount.setParkingArea(response.data!!)
            }
        }
    }

}