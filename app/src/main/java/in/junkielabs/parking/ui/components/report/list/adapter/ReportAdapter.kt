package `in`.junkielabs.parking.ui.components.report.list.adapter

import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.databinding.ReportListItemBinding
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

/**
 * Created by Niraj on 10-12-2021.
 */
class ReportAdapter(var reportViewModel: ReportViewModel) : PagingDataAdapter<ParamCheckInOut, ReportAdapter.ItemViewHolder>(ParamCheckInOut.diffCallback) {




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

    class ItemViewHolder private constructor(binding: ReportListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reportViewModel: ReportViewModel, item: ParamCheckInOut) {


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