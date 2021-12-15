package `in`.junkielabs.parking.components.api.models.vehicle

import `in`.junkielabs.parking.components.api.models.wheeler.ParamWheeler
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Created by Niraj on 24-11-2021.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class ParamVehicle(var number: String, var wheeler: ParamWheeler) : Parcelable {


}