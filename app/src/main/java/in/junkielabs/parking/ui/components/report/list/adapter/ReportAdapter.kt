package `in`.junkielabs.parking.ui.components.report.list.adapter

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ReportListItemBinding
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModel
import `in`.junkielabs.parking.utils.UtilDate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

/**
 * Created by Niraj on 10-12-2021.
 */
class ReportAdapter(var reportViewModel: ReportViewModel) :
    PagingDataAdapter<ParamCheckInOut, ReportAdapter.ItemViewHolder>(ParamCheckInOut.diffCallback) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(reportViewModel, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    /* ******************************************************************************** *
     *                                    ItemViewModel
     */

    class ItemViewHolder private constructor(var binding: ReportListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reportViewModel: ReportViewModel, item: ParamCheckInOut) {


            binding.reportListItemVehicleNumber.text = item.vehicleNumber

            if (item.finalAmount != null) {
                binding.reportListItemAmount.text = "₹${item.finalAmount}"
            }

            var timingText = UtilDate.getFormattedDateTime(
                item.inTimestamp,
                UtilDate.FORMAT_DATE_READABLE2,
                binding.root.context
            )
            if (item.outTimestamp != null) {
                timingText += " - " + UtilDate.getFormattedDateTime(
                    item.outTimestamp!!,
                    UtilDate.FORMAT_DATE_READABLE2,
                    binding.root.context
                )

            }
            binding.reportListItemTiming.text = timingText

            if (item.wheelerRate.type == ParkingConstants.Wheeler.TYPE_BIKE) {
                binding.reportListItemVehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_scooter
                    )
                )
            } else if (item.wheelerRate.type == ParkingConstants.Wheeler.TYPE_CAR) {
                binding.reportListItemVehicleImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_report_car
                    )
                )
            }


//            binding.bModel = item
//            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReportListItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }


}