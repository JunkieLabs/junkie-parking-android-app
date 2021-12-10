package `in`.junkielabs.parking.components.api.models.checkinout

import `in`.junkielabs.parking.components.api.models.wheeler.ParamWheelerRate
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize
import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 24-11-2021.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class ParamCheckInOut(
    var id: String,
    var parkingAccountId: String,
    var parkingAreaId: String,
    var qrCode: String,
    var vehicleId: String,
    var vehicleNumber: String,
    var inTimestamp: Long,
    var outTimestamp: Long?,
    var wheelerRate: ParamWheelerRate,
    var finalAmount: Double?,
    var status: Int
) : Parcelable {

    companion object {
        var diffCallback: DiffUtil.ItemCallback<ParamCheckInOut> =
            object : DiffUtil.ItemCallback<ParamCheckInOut>() {

                override fun areItemsTheSame(
                    @NonNull oldItem: ParamCheckInOut,
                    @NonNull newItem: ParamCheckInOut
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    @NonNull oldItem: ParamCheckInOut,
                    @NonNull newItem: ParamCheckInOut
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}