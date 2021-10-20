package `in`.junkielabs.parking.components.api.models.auth

import `in`.junkielabs.parking.components.api.routepoints.ApiPointAuth
import java.io.Serializable

data class ParamAuthVerifyReq(
    var token: String,
    var verificationType: String = ApiPointAuth.VERIFICATION_TYPE_GUARD,
): Serializable