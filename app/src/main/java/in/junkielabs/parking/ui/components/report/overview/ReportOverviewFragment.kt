package `in`.junkielabs.parking.ui.components.report.overview

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.models.reports.ParamReport
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ReportOverviewFragmentBinding
import `in`.junkielabs.parking.ui.base.FragmentBase
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModel
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModelFactory
import `in`.junkielabs.parking.ui.components.report.overview.viewmodel.ReportOverviewViewModel
import `in`.junkielabs.parking.ui.components.report.overview.viewmodel.ReportOverviewViewModelFactory
import `in`.junkielabs.parking.utils.UtilDate
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 06-12-2021.
 */
class ReportOverviewFragment : FragmentBase() {

    var vBinding: ReportOverviewFragmentBinding? = null

    private lateinit var mViewModel: ReportOverviewViewModel
/*    private val mViewModel: ReportOverviewViewModel by viewModels(
        factoryProducer = { ReportOverviewViewModelFactory(ApplicationMy.instance) }

//    ownerProducer = { return@viewModels this }
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this, ReportOverviewViewModelFactory(ApplicationMy.instance))
            .get(ReportOverviewViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vBinding = ReportOverviewFragmentBinding.inflate(inflater, container, false)
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

        mViewModel.mReport.observe(viewLifecycleOwner, {
            bindViewData(it)
        })
    }

    private fun bindViewData(paramReport: ParamReport) {

        if (vBinding == null) return

        vBinding!!.reportOverviewFragmentAmount.text = "₹${paramReport.amount}"
        vBinding!!.reportOverviewFragmentVehiclesCount.text = "${paramReport.total}"
        vBinding!!.reportOverviewFragmentTime.text = UtilDate.getFormattedDateTime(
            paramReport.startTime,
            UtilDate.FORMAT_DATE_READABLE2,
            requireContext()
        )

        var wheeler2 = paramReport.wheelers.firstOrNull { it.wheelerType == ParkingConstants.Wheeler.TYPE_BIKE }

        if(wheeler2==null){
            vBinding!!.reportOverviewFragment2wheeler.visibility = View.GONE
        }else{
            vBinding!!.reportOverviewFragment2wheeler.visibility = View.VISIBLE
            vBinding!!.reportOverviewFragment2wheelerCount.text = "${wheeler2!!.total}"
            vBinding!!.reportOverviewFragment2wheelerAmount.text = "₹${wheeler2!!.amount}"
        }

        var wheeler4 = paramReport.wheelers.firstOrNull { it.wheelerType == ParkingConstants.Wheeler.TYPE_CAR }

        if(wheeler4==null){
            vBinding!!.reportOverviewFragment4wheeler.visibility = View.GONE
        }else{
            vBinding!!.reportOverviewFragment4wheeler.visibility = View.VISIBLE
            vBinding!!.reportOverviewFragment4wheelerCount.text = "${wheeler4!!.total}"
            vBinding!!.reportOverviewFragment4wheelerAmount.text = "₹${wheeler4!!.amount}"
        }



    }

}