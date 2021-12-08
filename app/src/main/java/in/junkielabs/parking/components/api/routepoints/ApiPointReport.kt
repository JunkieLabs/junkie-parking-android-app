package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.reports.ParamReport
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by Niraj on 08-12-2021.
 */
interface ApiPointReport {

    @GET("report/parking-area")
    suspend fun reports(
        @Header("Authorization") token: String,
        @Query("parkingAreaId") parkingAreaId: String
    ): Response<ParamReport>
}