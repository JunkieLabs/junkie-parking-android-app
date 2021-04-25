package `in`.junkielabs.parking.ui.base

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.utils.UtilTheme
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


abstract class ActivityBase : AppCompatActivity(), AnkoLogger {


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

            info { "getStatusBarColor 1" }
            return ContextCompat.getColor(applicationContext, a.resourceId)
        }
        val colorRes = if(isDefaultStatus){

            R.color.design_default_color_background
        } else {

            R.color.colorPrimaryDark
        }
        info { "getStatusBarColor 2" }

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
        UtilTheme.setStatusBar(window.decorView, this, color, isDefaultStatus)
//        UtilTheme.setAccentStatusBar(window.decorView, this)
    }
    override fun setContentView(view: View) {
        super.setContentView(view)


        val color = getStatusBarColor()
        info { "setContentView Color: $color" }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = color
//        }
        UtilTheme.setStatusBar(window.decorView, this, color, isDefaultStatus)
//        UtilTheme.setAccentStatusBar(window.decorView, this)
    }


    /* ****************************************************************
                                                    Dialog
     */

    /* **************************************************************************************
  *                                    Account
  */
/*
    public suspend fun reqNewAccount(vendorType: Int): Boolean {
        return withContext(Dispatchers.IO){
            return@withContext reqAccount(vendorType)
        }


    }

    private suspend fun reqAccount(pVendorType: Int): Boolean {

        val accountType = UtilAccount.getAccountType(this)
        val authType = AccountConstants.Token.TOKEN_TYPE_FULL_ACCESS
        val options = Bundle()
        options.putBoolean(AccountConstants.Acccount.IS_RE_AUTHENTICATE, false)
        options.putBoolean(AccountConstants.Acccount.IS_NEW_ACCOUNT, true)
        options.putBoolean(AccountConstants.Acccount.IS_REMOVE_ACCOUNT, false)
        options.putString(AccountConstants.Acccount.ACCOUNT_TYPE, accountType)
        options.putString(AccountConstants.Acccount.AUTH_TYPE, authType)

        var vendorType = AccountConstants.Vendor.GOOGLE
        if (          pVendorType == AccountConstants.Vendor.GOOGLE) {
            vendorType = pVendorType
        }
        options.putInt(AccountConstants.Acccount.VENDOR, vendorType)


        return suspendCreateAccount(accountType, authType, options);
    }

    suspend fun suspendCreateAccount(accountType :String, authType :String,  options: Bundle): Boolean{
        return suspendCoroutine<Boolean> {
            val accountManagerCallback = AccountManagerCallback<Bundle> { future ->
                //LOGW(TAG, "on Req Account complete");
                try {
                    val b = future.result
                    info { "account manager callback: $b" }
                    //warn(b.toString())
                    if (b.getBoolean(AccountConstants.AccountResult.IS_SUCCESS, false)) {

                        it.resume(true)
                    } else {
                        it.resume(false)
                    }
                } catch (e: OperationCanceledException) {
                    //error{e.toString()}
                    e.printStackTrace()
                    it.resumeWithException(e)
//                    emitter.onError(e)
                } catch (e: IOException) {
                    //error(e.toString())
                    e.printStackTrace()
                    it.resumeWithException(e)
                } catch (e: AuthenticatorException) {
                    //error(e.toString())
                    e.printStackTrace()
                    it.resumeWithException(e)
                }
            }

//        warn { "aadd accout init" }
            val accountManager = AppAccountManager(this).accountManger
            accountManager.addAccount(accountType, authType, null, options, this,
                accountManagerCallback, null)
        }
    }*/


}
