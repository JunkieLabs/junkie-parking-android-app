package `in`.junkielabs.parking.ui.base

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout

abstract class FragmentBase : Fragment(), Toolbar.OnMenuItemClickListener {
    private var toolbar: Toolbar? = null

    private var _mToolbarTitle = ""
    private var _mIsCollapsed = true


    var toolbarTitle: String
        get() = _mToolbarTitle
        set(value) {

            _mToolbarTitle = value
            if (_mIsCollapsed) {
                toolbar?.title = value
            }else{
                toolbar?.title = ""

            }

        }

   /* var toolbarTitle: String = ""
        set(value) {
            toolbar?.title = value
            field = value
        }*/

    //protected abstract String getToolBarDefaultTitle();


    fun initToolbar(toolbar: Toolbar?, navigationDrawable: Int?, isBackNavigationEnable: Boolean) {

        this.toolbar = toolbar
        navigationDrawable?.let{
            toolbar?.setNavigationIcon(navigationDrawable)

        }
        toolbar?.setOnMenuItemClickListener(this)
        if (isBackNavigationEnable) {
            toolbar?.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return false
    }


    /* **********************************************************************************
  *                                      toolbar Collapsing
  */

    fun setToolbarCollapsibleListener(
        appBarLayout: AppBarLayout,
        collapsingToolbarLayout: CollapsingToolbarLayout
    ) {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            /* info { "${collapsingToolbarLayout.height} ${verticalOffset}  ${ViewCompat.getMinimumHeight(
                 collapsingToolbarLayout
             )}" }*/


            val collapsed = (collapsingToolbarLayout.height + verticalOffset) < 1.2 * ViewCompat.getMinimumHeight(
                collapsingToolbarLayout
            )

            if(_mIsCollapsed != collapsed){
                _mIsCollapsed =collapsed
//                info { "setToolbarCollapsibleListener $_mIsCollapsed" }
                toolbarTitle = _mToolbarTitle
            }


        })
    }

//    fun getToolbar(): Toolbar? {
//        return toolbar
//    }
}