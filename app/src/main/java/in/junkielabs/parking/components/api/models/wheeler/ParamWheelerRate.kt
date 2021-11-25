package `in`.junkielabs.parking.components.api.models.wheeler

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Niraj on 20-10-2021.
 */
@Parcelize
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
) : Parcelable {
}