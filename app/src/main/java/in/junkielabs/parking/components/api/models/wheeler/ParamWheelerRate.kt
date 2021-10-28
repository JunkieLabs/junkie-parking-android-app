package `in`.junkielabs.parking.components.api.models.wheeler

import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 20-10-2021.
 */
@JsonClass(generateAdapter = true)
class ParamWheelerRate(
    /** type */
    var type: Int,

    /** tyre count  */
    var tyreCount: Int,

    /** label */
    var label: String?,

    /**  rate */
    var rate: Float?
) {
}