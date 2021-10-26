package `in`.junkielabs.parking.components.api.models.auth

import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Created by niraj on 02-01-2020.
 */
@JsonClass(generateAdapter = true)
data class ParamAuthUser(
var id: String,
var username: String?,
var puid: String?,
var roles: List<Int>,
var guard: ParamGuard

): Serializable