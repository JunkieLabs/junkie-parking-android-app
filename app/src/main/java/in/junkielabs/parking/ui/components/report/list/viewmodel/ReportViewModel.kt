package `in`.junkielabs.parking.ui.components.report.list.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.api.repository.ApiRepoCheckInOut
import `in`.junkielabs.parking.components.datasource.checkinout.CheckInOutDataSource
import `in`.junkielabs.parking.components.parking.ParkingConstants
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlin.properties.Delegates

/**
 * Created by Niraj on 10-12-2021.
 */
class ReportViewModel(
    application: Application,
    var apiRepoCheckInOut: ApiRepoCheckInOut,
) : AndroidViewModel(application) {

    var mCurrentCheckInOutStatus = ParkingConstants.CheckInOut.STATUS_ACTIVE

    private var mParkingAreaId: String by Delegates.notNull<String>()

    fun initData(){
        mParkingAreaId = getApplication<ApplicationMy>().appAccount.getParkingAreaId().toString()


    }

    fun onStatusChange(status: Int): Boolean {
        if(mCurrentCheckInOutStatus == status){
            return false
        }
        mCurrentCheckInOutStatus = status
        return true
    }


    fun apiData(): Flow<PagingData<ParamCheckInOut>> {
//        var query = firestoreChapterRepo.query(mBookId)


        var pager = Pager(
            config = PagingConfig(pageSize = CheckInOutDataSource.PAZE_SIZE),
            pagingSourceFactory = {
                CheckInOutDataSource::class.java.getConstructor(ApiRepoCheckInOut::class.java, String::class.java, Int::class.java)
                    .newInstance(apiRepoCheckInOut, mParkingAreaId, mCurrentCheckInOutStatus)
            }
        )

            return pager.flow

            //.cachedIn(viewModelScope)
    }

}