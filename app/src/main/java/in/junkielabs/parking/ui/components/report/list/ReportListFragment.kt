package `in`.junkielabs.parking.ui.components.report.list

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.databinding.ReportListFragmentBinding
import `in`.junkielabs.parking.ui.base.FragmentBase
import `in`.junkielabs.parking.ui.components.report.ReportPagerAdapter
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Niraj on 06-12-2021.
 */
class ReportListFragment : FragmentBase() {

    var vBinding: ReportListFragmentBinding? = null



    private val mAdapter = ReportPagerAdapter()

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
        setupViewModel()

    }

    override fun onDestroy() {
        super.onDestroy()
        vBinding = null
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