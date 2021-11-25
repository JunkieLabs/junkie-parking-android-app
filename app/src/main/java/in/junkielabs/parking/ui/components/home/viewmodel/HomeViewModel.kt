package `in`.junkielabs.parking.ui.components.home.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.api.models.checkinout.ParamReqCheckInOut
import `in`.junkielabs.parking.components.api.models.parking.area.ParamParkingArea
import `in`.junkielabs.parking.components.api.models.wheeler.ParamWheelerRate
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoCheckInOut
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.tools.livedata.LiveDataEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 28-10-2021.
 */
class HomeViewModel(
    application: Application,
    var apiRepoAuth: ApiRepoAuth,
    var apiRepoCheckInOut: ApiRepoCheckInOut,
) : AndroidViewModel(application) {

    var bWheelerBike = MutableLiveData<ParamWheelerRate>()
    var bWheelerCar = MutableLiveData<ParamWheelerRate>()
    var bParkingArea = MutableLiveData<ParamParkingArea>()

    var mActiveWheelerType: Int? = null
    var bWheelerType = MutableLiveData<Int>()

    val bFormIsValid = MutableLiveData<Boolean>(false)
    private val _mEventErrorMessage = MutableLiveData<LiveDataEvent<String>>()
    val mEventErrorMessage: LiveData<LiveDataEvent<String>> = _mEventErrorMessage

    private val _mEventCheckInOut = MutableLiveData<LiveDataEvent<ParamCheckInOut>>()
    val mEventCheckInOut: LiveData<LiveDataEvent<ParamCheckInOut>> = _mEventCheckInOut

    private var _mFormVehicleNumber: String = ""
    private var _mFormPhoneNumber: String? = null

    init {

    }

    fun initData() {
        var parkingArea = getApplication<ApplicationMy>().appAccount.getParkingArea()
        if (parkingArea != null) {
            bParkingArea.postValue(parkingArea!!)
            var wheelerRates = parkingArea.rates


            for (wheelerRate in wheelerRates) {
                if (wheelerRate.type == ParkingConstants.Wheeler.TYPE_BIKE) {
                    bWheelerBike.postValue(wheelerRate)
                    setWheelerChecked(wheelerRate.type)
                } else if (wheelerRate.type == ParkingConstants.Wheeler.TYPE_CAR) {
                    bWheelerCar.postValue(wheelerRate)
                    if (mActiveWheelerType == null) {

                        setWheelerChecked(wheelerRate.type)
                    }
                }
            }
            bParkingArea.postValue(parkingArea!!)

        }
    }

    fun setWheelerChecked(wheelerType: Int) {
        if (mActiveWheelerType != wheelerType) {
            mActiveWheelerType = wheelerType
            bWheelerType.postValue(wheelerType)
        }
    }

    fun formVehicleNumber(vehicleNumber: String) {

        Log.d("HomeViewModel", "formVehicleNumber: ${vehicleNumber.length} ${ParkingConstants.Vehicle.INPUT_FORMAT.length}")
        this._mFormVehicleNumber = vehicleNumber;
        formEnable()
    }

    fun formPhoneNumber(phoneNumber: String) {
        this._mFormPhoneNumber = phoneNumber;
        formEnable()
    }

    private fun formEnable() {
        val isFormEnabled =
            _mFormVehicleNumber.length == ParkingConstants.Vehicle.INPUT_FORMAT.length
        bFormIsValid.postValue(isFormEnabled)
    }


    fun onSubmit() {
        if (bFormIsValid.value == true) {
             viewModelScope.launch {
                 apiCheckInOut()
             }
        }
    }


    /* ******************************************************************************************
     *                                              api
     */
    suspend fun apiCheckInOut() {
        var token = FirebaseToken.getToken()

        if (token == null) return


        var paramRepoCheckInOut = ParamReqCheckInOut(
            ApplicationMy.instance.appAccount.getParkingAreaId(),
            mActiveWheelerType?: ParkingConstants.Wheeler.TYPE_BIKE,
            _mFormVehicleNumber,
            _mFormPhoneNumber,
            null,
            null
        )
        var response = apiRepoCheckInOut.checkInOut(token, paramRepoCheckInOut)

        if (response.status == ApiResponse.Status.SUCCESS) {
            Log.i("AuthViewModel: result", response.data.toString())

            if(response.data?.checkInOut!=null){
                _mEventCheckInOut.postValue(LiveDataEvent(response.data!!.checkInOut))
            }
//
          /*  var data = response.data
            data?.guard?.let { getApplication<ApplicationMy>().appAccount.setGuard(it) }
            onCreateAccountSuccess(true)*/
        } else {
            if (response.status == ApiResponse.Status.ERROR) {


                var message = response.errorData?.message ?: response.message
                message?.let {
                    _mEventErrorMessage.postValue(LiveDataEvent(message))
                }
                Log.e("AuthViewModel: error", response.errorData.toString())
//                    info { "erer : ${it.message}" }

//                    _mEventErrorMessage.value = LiveDataEvent(it.message.toString())
//                }

            }
        }
    }


}