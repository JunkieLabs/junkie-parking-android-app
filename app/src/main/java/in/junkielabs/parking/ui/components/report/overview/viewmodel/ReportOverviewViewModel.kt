package `in`.junkielabs.parking.ui.components.report.overview.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.base.ApiResponse
import `in`.junkielabs.parking.components.api.models.reports.ParamReport
import `in`.junkielabs.parking.components.api.repository.ApiRepoReport
import `in`.junkielabs.parking.components.firebase.auth.FirebaseToken
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 08-12-2021.
 */
class ReportOverviewViewModel(application: Application,

                              var apiRepoReport: ApiRepoReport
) : AndroidViewModel(application) {

//    var mReportWheelers = MutableLiveData<List<ParamReportWheeler>>()

    var mReport = MutableLiveData<ParamReport>()

    fun initData(){
        viewModelScope.launch {
            apiGetReport()
        }

    }
    /* ****************************************************************************
     *                                  Api
     */

    private suspend fun apiGetReport() {
        var parkingAreaId = getApplication<ApplicationMy>().appAccount.getParkingAreaId()

        var token = FirebaseToken.getToken()
        if (parkingAreaId != null && token != null) {
            var response = apiRepoReport.forParkingArea(token, parkingAreaId);
            if (response.status == ApiResponse.Status.SUCCESS && response.data != null) {

//                Log.i("apiGetParkingArea", "${response.data}");
                mReport.postValue(response.data!!)

/*                if(response.data!!.wheelers !=null){
                    mReportWheelers.postValue(response.data!!.wheelers)
                }*/


            }
        }
    }
}