package `in`.junkielabs.parking.ui.common.scanner.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.api.models.checkinout.ParamReqCheckInOut
import `in`.junkielabs.parking.components.api.models.parking.area.ParamParkingArea
import `in`.junkielabs.parking.components.api.models.vehicle.ParamVehicle
import `in`.junkielabs.parking.components.api.repository.ApiRepoQrCode
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.tools.livedata.LiveDataEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 15-12-2021.
 */
class QrScannerViewModel(application: Application,
                         var apiRepoQrCode: ApiRepoQrCode
) : AndroidViewModel(application){


    var mIsInProgress = MutableLiveData<Boolean>()

    private val _mEventErrorMessage = MutableLiveData<LiveDataEvent<String>>()
    val mEventErrorMessage: LiveData<LiveDataEvent<String>> = _mEventErrorMessage

    private val _mEventVehicle = MutableLiveData<LiveDataEvent<ParamVehicle>>()
    val mEventVehicle: LiveData<LiveDataEvent<ParamVehicle>> = _mEventVehicle

    var mVehicle: ParamVehicle? = null

     fun onQrCode(qrCode: String){
        viewModelScope.launch {
            mVehicle = null
            mIsInProgress.postValue(true)
            apiQrCode(qrCode)
            mIsInProgress.postValue(false)
        }
    }
    /* ******************************************************************************************
   *                                              api
   */
    private suspend fun apiQrCode(qrCode:  String) {
        var token: String? = FirebaseToken.getToken() ?: return


        var response = apiRepoQrCode.getVehicle(token!!, qrCode)

        if (response.status == ApiResponse.Status.SUCCESS) {
            Log.i("AuthViewModel: result", response.data.toString())

            if(response.data!=null){
                mVehicle = response.data
                _mEventVehicle.postValue(LiveDataEvent(response.data!!))
            }

        } else {
            if (response.status == ApiResponse.Status.ERROR) {


                var message = response.errorData?.message ?: response.message
                message?.let {
                    _mEventErrorMessage.postValue(LiveDataEvent(message))
                }
                Log.e("AuthViewModel: error", response.errorData.toString())


            }
        }
    }


}