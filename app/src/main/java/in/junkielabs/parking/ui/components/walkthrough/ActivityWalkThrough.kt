package `in`.junkielabs.parking.ui.components.walkthrough

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.databinding.ActivityWalkThroughBinding
import `in`.junkielabs.parking.tools.timer.Stopwatch
import `in`.junkielabs.parking.tools.viewpager.transformer.ZoomOutPageTransformer
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.onboard.ActivityOnboard
import `in`.junkielabs.parking.ui.widgets.ViewPager2PagerListener
import `in`.junkielabs.parking.utils.UtilColor
import `in`.junkielabs.parking.utils.UtilTheme
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint

//https://material.io/components/buttons/android#theming-buttons
@AndroidEntryPoint
class ActivityWalkThrough : ActivityBase(),
    WalkThroughPagerAdapter.WalkThroughPagerAdapterListener {
    private lateinit var binding: ActivityWalkThroughBinding

//    var mStopwatch: Stopwatch? =null //= Stopwatch(10000, 1000)


//    var mIsStopWatchRunning = false
    private lateinit var mPagerAdapter: WalkThroughPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
        initToolbar(binding.toolbar)
        toolbarTitle = ""
//        initStopwatch()
        initPager()

    }


    override fun getStatusBarColor(): Int {
//        val startColor = Color.parseColor("#FA2AD0")
//        val a = TypedValue()
//        theme.resolveAttribute(R.attr.colorSurface, a, true)

//        val endColor = ContextCompat.getColor(applicationContext, a.resourceId)
//        val endColor = ContextCompat.getColor(context, R.color.yellow)
//        return ColorUtils.blendARGB(Color.parseColor("#FA2AD0"), endColor, 0.12F)
//        return UtilColor.getColorForAlpha(Color.parseColor("#FA2AD0"), 0.12F)
        return ContextCompat.getColor(this, R.color.colorAccentDark)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }
    override fun onWalkThroughPagerAdapterItemClicked(position: Int) {

    }


    /*private fun moveNext() {
        var currentItem = 0

        currentItem = if(binding.viewPager2.currentItem >= 2){
            0
        } else {
            binding.viewPager2.currentItem + 1
        }
//        info { "currentItem: $currentItem" }
        binding.viewPager2.setCurrentItem(currentItem,true)
        if(mIsStopWatchRunning){
//            mStopwatch.cancel()

        }

        Handler(Looper.getMainLooper()).post(Runnable { initStopwatch() })
//        initStopwatch()
//        view_pager2.setCurrentItem(,true)
    }*/



    fun initPager() {

        mPagerAdapter = WalkThroughPagerAdapter()
        binding.viewPager2.adapter = mPagerAdapter
        mPagerAdapter.setWalkThroughPagerAdapterListener(this)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerDots.setPager(ViewPager2PagerListener(binding.viewPager2))

        binding.activityWalkThroughBtnNext.setOnClickListener {

            moveNext()
        }
        binding.activityWalkThroughBtnPrev.isEnabled = false
        binding.activityWalkThroughBtnNext.isEnabled = true

        binding.activityWalkThroughBtnPrev.setOnClickListener {

            movePrev()
        }
        binding.viewPager2.setPageTransformer(ZoomOutPageTransformer())

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.activityWalkThroughBtnPrev.isEnabled = position != 0
            }

        })



    }

    fun movePrev() {
        val currentItem = binding.viewPager2.currentItem

        binding.viewPager2.setCurrentItem(currentItem - 1, true)
//        binding.activityWalkThroughBtnPrev.isEnabled = currentItem != 1
    }

    fun moveNext() {
        val currentItem = binding.viewPager2.currentItem

        Log.i("ActivityWalkThrough", "moveNext $currentItem")
        if (currentItem == 2) {
            //            Todo startAuth()
            startActivityOnboard()

        } else {
            binding.viewPager2.setCurrentItem(currentItem + 1, true)
//            binding.activityWalkThroughBtnPrev.isEnabled = true

        }
    }

    /* ***************************************************************************
     *                      walk through
     */

    private fun startActivityOnboard() {
        val i = Intent(this, ActivityOnboard::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.data = intent.data

        startActivity(i)
        finish()
    }

}