package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOutsResult
import `in`.junkielabs.parking.components.api.models.checkinout.ParamReqCheckInOut
import `in`.junkielabs.parking.components.api.models.checkinout.ParamResCheckInOut
import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Niraj on 24-11-2021.
 */
interface ApiPointCheckInOut {

    @POST("check-in-out")
    suspend fun checkInOut(
        @Header("Authorization") token: String,
        @Body reqCheckInOut: ParamReqCheckInOut
    ): Response<ParamResCheckInOut>


    @GET("check-in-outs")
    suspend fun checkInOuts(
        @Header("Authorization") token: String,
        @Query("parkingAreaId") parkingAreaId: String,
        @Query("status") status: String,
        @Query("key") key: String?
    ): Response<ParamCheckInOutsResult>

}