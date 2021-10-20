package `in`.junkielabs.parking.components.api.models.auth

import `in`.junkielabs.parking.components.api.models.guard.ParamGuard
import java.io.Serializable

/**
 * Created by niraj on 02-01-2020.
 */
data class ParamAuthUser(
var _id: String,
var username: String?,
var puid: String?,
var role: Number,
var guard: ParamGuard

): Serializable