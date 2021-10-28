package `in`.junkielabs.parking.ui.components.auth.viewmodel

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 22-10-2021.
 */
class AuthViewModel(application: Application,
                    var apiRepoAuth: ApiRepoAuth,
                    var apiRepoGuard: ApiRepoGuard,
                    var apiRepoParkingArea: ApiRepoParkingArea
) : AndroidViewModel(application) {

    var mAccountAction: String? = null
        private set

    private val _mEventAccountAction = MutableLiveData<LiveDataEvent<String>>()
    val mEventAccountAction: LiveData<LiveDataEvent<String>> = _mEventAccountAction

    private val _mEventIsCreated = MutableLiveData<LiveDataEvent<Boolean>>()
    val mEventIsCreated: LiveData<LiveDataEvent<Boolean>> = _mEventIsCreated

    private val _mEventProgress = MutableLiveData<LiveDataEvent<Boolean>>()
    val mEventProgress: LiveData<LiveDataEvent<Boolean>> = _mEventProgress

   /* private val _mEventIsUpdated = MutableLiveData<LiveDataEvent<Boolean>>()
    val mEventIsUpdated: LiveData<LiveDataEvent<Boolean>> = _mEventIsUpdated

*/
    private val _mEventLoggedOut = MutableLiveData<LiveDataEvent<Boolean>>()
    val mEventLoggedOut: LiveData<LiveDataEvent<Boolean>> = _mEventLoggedOut

    override fun onCleared() {
        super.onCleared()
    }
    fun initData(accountAction: String?) {
        mAccountAction = accountAction
//        phoneNumber?.let { bUsername.value = it }
        accountAction?.let { onAccountAction(it) }

    }

    fun onAccountAction(action: String) {
        _mEventAccountAction.postValue(LiveDataEvent(action))
    }

    fun getCurrentAction(): String? {
        return mAccountAction
    }

    fun onAuthorized() {

        Log.i("AuthViewModel", "onAuthorized")
        viewModelScope.launch {
            // var token = FirebaseToken.getUserIdToken()
//            Log.i("AuthViewModel Token:", token.toString());

             apiVerify()
        }
    }

    private fun onCreateAccountSuccess(isCreated: Boolean) {
        _mEventProgress.postValue(LiveDataEvent(true))
        _mEventIsCreated.postValue(LiveDataEvent(isCreated))
    }

    fun logout(){

        _mEventProgress.postValue(LiveDataEvent(true))
        viewModelScope.launch {
            delay(120)
            removeAccount()
            _mEventProgress.postValue(LiveDataEvent(false))
            _mEventLoggedOut.postValue(LiveDataEvent(true))
//            _mEventAccountAction.value = LiveDataEvent(AccountConstants.Account.ACTION_SIGNUP)

        }
    }

    private fun removeAccount(){
        getApplication<ApplicationMy>().appAccount.reset()
    }

    fun reAuthenticate() {

        _mEventProgress.postValue(LiveDataEvent(true))
        viewModelScope.launch {
            delay(150)
            removeAccount()
//            info { "isremoved Account : $isremoved" }

            _mEventProgress.postValue(LiveDataEvent(false))
            mAccountAction = AccountConstants.Account.ACTION_SIGNIN
            _mEventAccountAction.postValue(LiveDataEvent(AccountConstants.Account.ACTION_SIGNIN))
        }

    }



    /* **************************************************************************
     *                              Api
     */

    private suspend fun apiVerify(){

            var token = FirebaseToken.getUserIdToken()

            if(token==null)return


            var response = apiRepoAuth.verify(token)

            if(response.status == ApiResponse.Status.SUCCESS){
                Log.i("AuthViewModel: result", response.data.toString())
//
                var data = response.data
                data?.guard?.let { getApplication<ApplicationMy>().appAccount.setGuard(it) }
                onCreateAccountSuccess(true)
            }else{
                if (response.status == ApiResponse.Status.ERROR) {


                    Log.e("AuthViewModel: error", response.errorData.toString())
//                    info { "erer : ${it.message}" }

//                    _mEventErrorMessage.value = LiveDataEvent(it.message.toString())
//                }

                }
                onCreateAccountSuccess(false)
            }


    }

}