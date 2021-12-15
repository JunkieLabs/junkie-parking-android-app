package `in`.junkielabs.parking.components.api.models.wheeler

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Niraj on 15-12-2021.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class ParamWheeler(
    /** type */
    var type: Int,

    /** tyre count  */
    var tyreCount: Int,

    /** label */
    var label: String?,

) : Parcelable {
}