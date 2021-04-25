package `in`.junkielabs.parking.ui.widgets

import androidx.annotation.NonNull
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by niraj on 24-04-2021.
 */


class ViewPager2PagerListener(@NonNull pager: ViewPager2) : PagerIndicatorView.PagerListener{

    private val pager: ViewPager2
    private var pageChangeListener: PageChangeListener
    private var callback: PagerIndicatorView.PagerCallback? = null

    init {

        this.pager = pager
        pageChangeListener = PageChangeListener()
    }

    override fun setPagerCallback(callback: PagerIndicatorView.PagerCallback?) {
        this.callback = callback
        if (callback != null) {
            pager.registerOnPageChangeCallback(pageChangeListener)
            pageChangeListener.onPageScrolled(pager.currentItem, 0f, 0)
        } else {
            pager.unregisterOnPageChangeCallback(pageChangeListener)
        }
    }

    private inner class PageChangeListener : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            callback!!.setItemCount(pager.adapter!!.itemCount)
            callback!!.setPageScrolled(position, positionOffset)
        }
    }

}