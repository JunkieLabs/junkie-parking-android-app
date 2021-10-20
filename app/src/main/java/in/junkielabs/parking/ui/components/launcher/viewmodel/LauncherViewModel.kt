package `in`.junkielabs.parking.ui.components.launcher.viewmodel

import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.components.account.AccountPreference
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoGuard
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import `in`.junkielabs.parking.tools.livedata.LiveDataEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Niraj on 20-10-2021.
 */
class LauncherViewModel(application: Application,
                        var apiRepoAuth: ApiRepoAuth,
                        var apiRepoGuard: ApiRepoGuard) : AndroidViewModel(application) {

    private val _mEventAccountState = MutableLiveData<LiveDataEvent<Int>>()
    val mEventAccountState: LiveData<LiveDataEvent<Int>> = _mEventAccountState

    override fun onCleared() {
        super.onCleared()
    }

    fun checkHasAccount() {

        viewModelScope.launch {
            val accountState = getAccountState()


            if(accountState == AccountConstants.AccountUser.STATE_AUTHORIZED || accountState == AccountConstants.AccountUser.STATE_WAITING){
                Log.i("LauncherViewModel","Enrollment info called for account state ${AccountConstants.AccountUser.STATE_AUTHORIZED}")


            }
            _mEventAccountState.postValue(LiveDataEvent(accountState))

//            (application as ApplicationMy).appAccount.setHasAccount(hasAccount)

        }


    }

    private suspend fun getAccountState() = suspendCoroutine<Int> {

        var currentUser = FirebaseAuth.getInstance().currentUser
        var parkingAreaId = AccountPreference.getInstance().getParkingAreaId()
        var guardId = AccountPreference.getInstance().getGuardId()

        if(parkingAreaId!=null){
            it.resume(AccountConstants.AccountUser.STATE_AUTHORIZED)
        }else if(guardId!=null){
            it.resume(AccountConstants.AccountUser.STATE_WAITING)

        }else if(currentUser!=null){
            it.resume(AccountConstants.AccountUser.STATE_REAUTH)

        }else{
            it.resume(AccountConstants.AccountUser.STATE_NOT_EXIST)

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

    suspend fun apiGetGuardInfo(){
        var guardId = AccountPreference.getInstance().getGuardId()

        var token = FirebaseToken.getToken()
        if(guardId!=null && token!=null){
            var response  = apiRepoGuard.getById(token, guardId);
        }
    }


}