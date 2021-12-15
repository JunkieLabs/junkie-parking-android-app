package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import `in`.junkielabs.parking.components.api.models.vehicle.ParamVehicle
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Niraj on 15-12-2021.
 */
interface ApiPointQrCode {

    @GET("qrcode/vehicle/{code}")
    suspend fun vehicle(
        @Header("Authorization") token: String,
        @Path("code") code: String
    ): Response<ParamVehicle>
}