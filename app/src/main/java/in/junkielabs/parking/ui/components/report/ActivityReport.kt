package `in`.junkielabs.parking.ui.components.report

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityReportBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

class ActivityReport : ActivityBase() {


    private lateinit var mPagerAdapter: ReportPagerAdapter
    private lateinit var binding: ActivityReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
        initToolbar(R.drawable.ic_arrow_back, binding.toolbar)
        toolbarTitle = ""
        initPager()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun initPager() {

        mPagerAdapter = ReportPagerAdapter(this)

        binding.viewPager2.adapter = mPagerAdapter
//        mPagerAdapter.setWalkThroughPagerAdapterListener(this)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        with(binding.viewPager2){
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = mPagerAdapter.itemCount
        }

        TabLayoutMediator(binding.tablayout, binding.viewPager2) { tab, position ->

            val tabName = when (position) {
                0 -> "All list"
                1 -> "Overview"

                else -> "Overview"
            }
            tab.text = tabName
        }.attach()
       /* val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.offset_12)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset_16)
        binding.viewPager2.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }*/




    }
}