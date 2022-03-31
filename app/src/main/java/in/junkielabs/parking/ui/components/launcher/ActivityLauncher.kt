package `in`.junkielabs.parking.ui.components.launcher

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.auth.ActivityAuth
import `in`.junkielabs.parking.ui.components.home.ActivityHome
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModel
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModelFactory
import `in`.junkielabs.parking.ui.components.wait.ActivityWait
import `in`.junkielabs.parking.ui.components.walkthrough.ActivityWalkThrough
import `in`.junkielabs.parking.utils.UtilTheme
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityLauncher : ActivityBase() {

    private val mViewModel: LauncherViewModel by viewModels(
        factoryProducer = { LauncherViewModelFactory(ApplicationMy.instance) }

//    ownerProducer = { return@viewModels this }
    )

    private val mActivityResultAuth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            info{"mActivityResultSignin: ${result.data}"}
            onAuthenticationResult(result.data, result.resultCode)
        }

    private val mActivityResultAuthRemove =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            info{"mActivityResultAuthRemove: ${result.data}"}
            onAuthenticationRemoveResult(result.data, result.resultCode)
        }

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {

        Handler(Looper.getMainLooper()).postDelayed({ mViewModel.checkHasAccount() }, 100)

        mViewModel.mEventAccountState.observe(this,
            LiveDataObserver { t ->

                when (t) {
                    AccountConstants.AccountUser.STATE_NOT_EXIST -> {
//                        mViewModel.reqAuth(this)
                        startWalkThrough()
                    }
                    AccountConstants.AccountUser.STATE_AUTHORIZED -> {
                        startApp()
                    }
                    AccountConstants.AccountUser.STATE_REAUTH -> {
                          startActivityReAuth()
                    }
                    AccountConstants.AccountUser.STATE_WAITING -> {
                        startActivityWaiting()
                    }
                }


            }
        )

        mViewModel.mEventExit.observe(this, LiveDataObserver{
            t ->

            if(mViewModel.mShouldRemoveAccount){
                startActivityRemove()
            }else{
                finish()
            }

        })
    }

    override fun getStatusBarColor(): Int {
//        val startColor = Color.parseColor("#FA2AD0")
//        val a = TypedValue()
//        theme.resolveAttribute(R.attr.colorSurface, a, true)

//        val endColor = ContextCompat.getColor(applicationContext, a.resourceId)
//        val endColor = ContextCompat.getColor(context, R.color.yellow)
//        return ColorUtils.blendARGB(Color.parseColor("#FA2AD0"), endColor, 0.12F)
        return ContextCompat.getColor(
            this,
            R.color.colorAccentDark
        )//UtilColor.getColorForAlpha(, 0.12F)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }

    private fun startWalkThrough() {
        val i = Intent(this, ActivityWalkThrough::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.data = intent.data

        startActivity(i)
        finish()
    }

    /* **************************************************************************************
                                            App
     */


    private fun startApp() {
        val i = Intent(this, ActivityHome::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.data = intent.data

        startActivity(i)
        finish()
    }

    private fun startActivityWaiting(){
        val i = Intent(this, ActivityWait::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.data = intent.data

        startActivity(i)
        finish()
    }

    /* ***************************************************************************
        *                               Auth
        */

    private fun startActivityReAuth() {
        val i = Intent(this, ActivityAuth::class.java)
        i.putExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION, AccountConstants.Account.ACTION_REAUTH)
        mActivityResultAuth.launch(i)
//        mActivityResultAuth.launch(i)
//       i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        startActivity(i)
        //        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.data = intent.data

//        startActivity(i)
//        finish()
    }

    private fun startActivityRemove() {
        val i = Intent(this, ActivityAuth::class.java)
        i.putExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION, AccountConstants.Account.ACTION_REMOVE)
        mActivityResultAuthRemove.launch(i)
    }

    private fun onAuthenticationResult(data: Intent?, resultCode: Int) {
        if (resultCode == RESULT_OK) {

            mViewModel.onSignInResult()
            // startActivityAuth()

        }
//        mViewModel.onSignInResult()
       /* if (resultCode == RESULT_OK) {


            // startActivityAuth()

        } else {

            //finishAuthenticationWithFailMessage("Canceled")

        }*/

    }

    private fun onAuthenticationRemoveResult(data: Intent?, resultCode: Int) {
        if (resultCode == RESULT_OK) {
        startWalkThrough()

        }else{
            finish()
        }
//        mViewModel.onSignInResult()
        /* if (resultCode == RESULT_OK) {


             // startActivityAuth()

         } else {

             //finishAuthenticationWithFailMessage("Canceled")

         }*/

    }
}