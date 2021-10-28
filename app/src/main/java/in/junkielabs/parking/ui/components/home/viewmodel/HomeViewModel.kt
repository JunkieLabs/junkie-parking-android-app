package `in`.junkielabs.parking.ui.components.home.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.models.parking.area.ParamParkingArea
import `in`.junkielabs.parking.components.api.models.wheeler.ParamWheelerRate
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.parking.ParkingConstants
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * Created by Niraj on 28-10-2021.
 */
class HomeViewModel(application: Application,
                    var apiRepoAuth: ApiRepoAuth,
) : AndroidViewModel(application) {

    var bWheelerBike = MutableLiveData<ParamWheelerRate>()
    var bWheelerCar = MutableLiveData<ParamWheelerRate>()
    var bParkingArea = MutableLiveData<ParamParkingArea>()

    var mActiveWheelerType: Int? = null
    var bWheelerType = MutableLiveData<Int>()

    init {

    }

    fun initData(){
       var parkingArea =  getApplication<ApplicationMy>().appAccount.getParkingArea()
        if(parkingArea!=null){
            bParkingArea.postValue(parkingArea!!)
            var wheelerRates = parkingArea.rates


            for (wheelerRate in wheelerRates){
                if(wheelerRate.type == ParkingConstants.Wheeler.TYPE_BIKE){
                 bWheelerBike.postValue(wheelerRate)
                    setWheelerChecked(wheelerRate.type)
                } else if(wheelerRate.type == ParkingConstants.Wheeler.TYPE_CAR){
                    bWheelerCar.postValue(wheelerRate)
                    if(mActiveWheelerType==null){

                        setWheelerChecked(wheelerRate.type)
                    }
                }
            }
            bParkingArea.postValue(parkingArea!!)

        }
    }

    fun setWheelerChecked(wheelerType: Int) {
        if(mActiveWheelerType!=wheelerType){
            mActiveWheelerType = wheelerType
            bWheelerType.postValue(wheelerType)
        }
    }
}