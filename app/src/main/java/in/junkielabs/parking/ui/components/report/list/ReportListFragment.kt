package `in`.junkielabs.parking.ui.components.report.list

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ReportListFragmentBinding
import `in`.junkielabs.parking.ui.base.FragmentBase
import `in`.junkielabs.parking.ui.components.report.list.adapter.ReportAdapter
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModel
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 06-12-2021.
 */
class ReportListFragment : FragmentBase() {

    var vBinding: ReportListFragmentBinding? = null


    private var mListJob: Job? = null
    private lateinit var  mAdapter:ReportAdapter

    private lateinit var mViewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this, ReportViewModelFactory(ApplicationMy.instance))
            .get(ReportViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vBinding = ReportListFragmentBinding.inflate(inflater, container, false)
            .apply {
//                bViewModel = mViewModel
            }
        setHasOptionsMenu(true)
        return vBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupViewModel()

        vBinding?.reportListFragmentFilterButton?.setOnClickListener {
            showMenu(it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        vBinding = null
    }

    private fun setupAdapter(){

        mAdapter = ReportAdapter(mViewModel)

        vBinding?.recyclerView?.adapter = mAdapter
    }

    private fun setupViewModel() {
        mViewModel.initData()
        setupPagination()

    }

    fun setupPagination(){
        mListJob?.cancel()
        mListJob = lifecycleScope.launch {
            mViewModel.apiData().collectLatest {

                Log.d("ReportListFragment: ", "apiData")
                mAdapter.submitData(it)
            }

        }
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(R.menu.report_checkinout_menu, popup.menu)

        popup.setOnMenuItemClickListener { it ->
            if(it.itemId == R.id.menu_item_report_checkinout_in){
                onStatusChange(ParkingConstants.CheckInOut.STATUS_ACTIVE)
            }else if(it.itemId == R.id.menu_item_report_checkinout_out){
                onStatusChange(ParkingConstants.CheckInOut.STATUS_COMPLETED)
            }
            return@setOnMenuItemClickListener true }
//        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
//            // Respond to menu item click.
//        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }
        // Show the popup menu.
        popup.show()
    }

    private fun onStatusChange(status: Int) {

        if(status == ParkingConstants.CheckInOut.STATUS_ACTIVE){
            vBinding?.reportListFragmentFilterButton?.text = "Checked In"
        } else if(status == ParkingConstants.CheckInOut.STATUS_COMPLETED){
            vBinding?.reportListFragmentFilterButton?.text = "Checked Out"
        }
        var isChanged = mViewModel.onStatusChange(status)
        if(isChanged){
            setupPagination()
        }

    }


}