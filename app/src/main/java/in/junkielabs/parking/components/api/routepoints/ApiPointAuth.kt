package `in`.junkielabs.parking.components.api.routepoints

import `in`.junkielabs.parking.components.api.models.auth.ParamAuthUser
import `in`.junkielabs.parking.components.api.models.auth.ParamAuthVerify
import `in`.junkielabs.parking.components.api.models.auth.ParamAuthVerifyReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Niraj on 20-10-2021.
 */
interface ApiPointAuth {

    companion object {
        const val VERIFICATION_TYPE_GUARD = "guard"
    }

    @POST("auth/verify")
    suspend fun verify(@Body req: ParamAuthVerifyReq): Response<ParamAuthVerify>


}