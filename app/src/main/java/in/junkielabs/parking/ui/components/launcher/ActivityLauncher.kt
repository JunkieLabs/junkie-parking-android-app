package `in`.junkielabs.parking.ui.components.launcher

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.home.ActivityHome
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModel
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModelFactory
import `in`.junkielabs.parking.ui.components.walkthrough.ActivityWalkThrough
import `in`.junkielabs.parking.utils.UtilTheme
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info


@AndroidEntryPoint
class ActivityLauncher : ActivityBase() {

    private val mViewModel: LauncherViewModel by viewModels(
        factoryProducer = { LauncherViewModelFactory(ApplicationMy.instance) }

//    ownerProducer = { return@viewModels this }
    )

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
        setUpViewModel()
    }

    private fun setUpViewModel() {

        Handler(Looper.getMainLooper()).postDelayed({ mViewModel.checkHasAccount() }, 700)

        mViewModel.mEventAccountState.observe(this,
            LiveDataObserver { t ->

                info { "Accounnt State: $t" }
                when (t) {
                    AccountConstants.AccountUser.STATE_NOT_EXIST -> {
//                        mViewModel.reqAuth(this)
                        startWalkThrough()
                    }
                    AccountConstants.AccountUser.STATE_AUTHORIZED -> {
                        startApp()
                    }
                    AccountConstants.AccountUser.STATE_REAUTH -> {
                       // mViewModel.reqReAuth(this)
                    }
                }


            }
        )
    }

    override fun getStatusBarColor(): Int {
//        val startColor = Color.parseColor("#FA2AD0")
//        val a = TypedValue()
//        theme.resolveAttribute(R.attr.colorSurface, a, true)

        info { "getStatusBarColor 1" }
//        val endColor = ContextCompat.getColor(applicationContext, a.resourceId)
//        val endColor = ContextCompat.getColor(context, R.color.yellow)
//        return ColorUtils.blendARGB(Color.parseColor("#FA2AD0"), endColor, 0.12F)
        return ContextCompat.getColor(this, R.color.colorAccentDark)//UtilColor.getColorForAlpha(, 0.12F)
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
}