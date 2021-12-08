package `in`.junkielabs.parking.ui.components.report

import `in`.junkielabs.parking.ui.components.report.list.ReportListFragment
import `in`.junkielabs.parking.ui.components.report.overview.ReportOverviewFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
/**
 * Created by Niraj on 06-12-2021.
 */
class ReportPagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            ReportListFragment()
        }else{
            ReportOverviewFragment()
        }
    }


}

