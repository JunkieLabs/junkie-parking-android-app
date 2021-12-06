package `in`.junkielabs.parking.ui.components.report.fragments

import `in`.junkielabs.parking.databinding.ReportOverviewFragmentBinding
import `in`.junkielabs.parking.ui.base.FragmentBase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Niraj on 06-12-2021.
 */
class ReportOverviewFragment : FragmentBase() {

     var vBinding: ReportOverviewFragmentBinding? = null

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

    }

    override fun onDestroy() {
        super.onDestroy()
        vBinding = null
    }

}