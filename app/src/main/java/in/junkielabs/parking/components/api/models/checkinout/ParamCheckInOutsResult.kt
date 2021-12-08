package `in`.junkielabs.parking.components.api.models.checkinout

import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 08-12-2021.
 */
@JsonClass(generateAdapter = true)
data class ParamCheckInOutsResult(
    var key:  String,
    var result: List<ParamCheckInOut>,
    var pageSize: Int
) {


}