package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Niraj on 20-10-2021.
 */
interface ApiPointGuard {

    @GET("guard/{id}")
    suspend fun byId(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ParamGuard>

}