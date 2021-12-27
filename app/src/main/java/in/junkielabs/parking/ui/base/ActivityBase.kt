package `in`.junkielabs.parking.ui.base

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.utils.UtilTheme
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

abstract class ActivityBase : AppCompatActivity() {


    var isDefaultStatus =false

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }



//    override fun attachBaseContext(newBase: Context) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
//    }

    var toolbarTitle: String
        get() = supportActionBar?.title.toString()
        set(value) {
            val actionBar = supportActionBar
            actionBar?.title = value
//            toolbar.title = value
        }

    fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

    }


    fun initToolbar(resId: Int, toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        setToolbarNavigationIcon(resId, toolbar)
    }

    fun setStatusDefault(boolean: Boolean){
        isDefaultStatus  = boolean
    }

    open fun getStatusBarColor(): Int {

        if(isDefaultStatus){
            val a = TypedValue()
            theme.resolveAttribute(R.attr.colorSurface, a, true)

            return ContextCompat.getColor(applicationContext, a.resourceId)
        }
        val colorRes = if(isDefaultStatus){

            R.color.design_default_color_background
        } else {

            R.color.colorPrimaryDark
        }

        return ContextCompat.getColor(applicationContext, colorRes)
    }


    private fun setToolbarNavigationIcon(resId: Int, toolbar: Toolbar) {
        toolbar.setNavigationIcon(resId)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)


        val color = getStatusBarColor()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = color
//        }
        setStatusBar(color)

//        UtilTheme.setAccentStatusBar(window.decorView, this)
    }
    override fun setContentView(view: View) {
        super.setContentView(view)


        val color = getStatusBarColor()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = color
//        }
        setStatusBar(color)

//        UtilTheme.setAccentStatusBar(window.decorView, this)
    }

    open fun setStatusBar(color:  Int){
        UtilTheme.setStatusBar(window.decorView, this, color, isDefaultStatus)
    }





}
