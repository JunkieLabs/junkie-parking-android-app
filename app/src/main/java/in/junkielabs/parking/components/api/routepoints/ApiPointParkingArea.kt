package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.parking.area.ParamParkingArea
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Niraj on 28-10-2021.
 */
interface ApiPointParkingArea {

    @GET("parking-area/{id}")
    suspend fun byId(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ParamParkingArea>
}