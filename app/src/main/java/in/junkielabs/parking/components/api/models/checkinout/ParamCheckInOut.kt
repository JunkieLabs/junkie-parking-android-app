package `in`.junkielabs.parking.components.api.models.checkinout

import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 24-11-2021.
 */
@JsonClass(generateAdapter = true)
class ParamCheckInOut(
    var id: String,
    var parkingAccountId: String,
    var parkingAreaId: String,
    var qrCode: String,
    var vehicleId: String,
    var vehicleNumber: String,
    var inTimestamp: Long,
    var outTimestamp: Long?,
    var wheeler: Int,
    var finalAmount: Double?,
    var status: Int
) {
}