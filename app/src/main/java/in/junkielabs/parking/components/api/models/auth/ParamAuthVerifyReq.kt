package `in`.junkielabs.parking.components.api.models.auth

import `in`.junkielabs.parking.components.api.routepoints.ApiPointAuth
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class ParamAuthVerifyReq(
    var token: String,
    var verificationType: String = ApiPointAuth.VERIFICATION_TYPE_GUARD,
): Serializable