package `in`.junkielabs.parking.ui.components.report.list

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.databinding.ReportListFragmentBinding
import `in`.junkielabs.parking.ui.base.FragmentBase
import `in`.junkielabs.parking.ui.components.report.ReportPagerAdapter
import `in`.junkielabs.parking.ui.components.report.list.adapter.ReportAdapter
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModel
import `in`.junkielabs.parking.ui.components.report.list.viewmodel.ReportViewModelFactory
import `in`.junkielabs.parking.ui.components.report.overview.viewmodel.ReportOverviewViewModel
import `in`.junkielabs.parking.ui.components.report.overview.viewmodel.ReportOverviewViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 06-12-2021.
 */
class ReportListFragment : FragmentBase() {

    var vBinding: ReportListFragmentBinding? = null



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
        lifecycleScope.launch {
            mViewModel.apiData().collectLatest {
                mAdapter.submitData(it)
            }

        }



    }
}