package `in`.junkielabs.parking.components.api.models.checkinout

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Created by Niraj on 24-11-2021.
 */
@JsonClass(generateAdapter = true)
data class ParamReqCheckInOut(
    var parkingAreaId: String?,
    var wheelerType: Int,
    var vehicleNumber: String,
    var phone: String?,
    var email: String?,
    var name: String?

): Serializable {
}