package `in`.junkielabs.parking.ui.components.onboard

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.databinding.ActivityOnboardBinding
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.auth.ActivityAuth
import `in`.junkielabs.parking.ui.components.home.ActivityHome
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModel
import `in`.junkielabs.parking.ui.components.launcher.viewmodel.LauncherViewModelFactory
import `in`.junkielabs.parking.ui.components.onboard.viewmodel.OnboardViewModel
import `in`.junkielabs.parking.ui.components.onboard.viewmodel.OnboardViewModelFactory
import `in`.junkielabs.parking.utils.UtilTheme
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info

@AndroidEntryPoint
class ActivityOnboard : ActivityBase() {

    private val mViewModel: OnboardViewModel by viewModels(
        factoryProducer = { OnboardViewModelFactory(ApplicationMy.instance) }

//    ownerProducer = { return@viewModels this }
    )

    private val mActivityResultAuth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            info{"mActivityResultSignin: ${result.data}"}
            onAuthenticationResult(result.data, result.resultCode)
        }

    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
        Glide.with(binding.activityOnboardImage)
            .load(R.drawable.pic_create_account)
            .thumbnail(0.5f)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //                    .transform(new CircleTransform(mContext))
            //                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.activityOnboardImage)

        binding.activityOnboardGoogleBtn.setOnClickListener {
            startActivityAuth()
        }
        setUpViewModel()
    }

    override fun getStatusBarColor(): Int {
        info { "getStatusBarColor 1" }
        return ContextCompat.getColor(this, R.color.colorAccentDark)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }

    private fun setUpViewModel() {
        mViewModel.mEventAccountState.observe(this,
            LiveDataObserver { t ->

                info { "mEventAccountState: $t" }
                when (t) {
                    AccountConstants.AccountUser.STATE_NOT_EXIST -> {
//                        mViewModel.reqAuth(this)

                    }
                    AccountConstants.AccountUser.STATE_AUTHORIZED -> {
                        startApp()
                    }

                }


            }
        )
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

    /* ***************************************************************************
        *                               Auth
        */

    private fun startActivityAuth() {
        info { "startActivityAuth" }
        val i = Intent(this, ActivityAuth::class.java)
         i.putExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION, AccountConstants.Account.ACTION_SIGNIN)
        mActivityResultAuth.launch(i)
    }

    private fun onAuthenticationResult(data: Intent?, resultCode: Int) {

        if (resultCode == RESULT_OK) {

            mViewModel.onSignInResult()
           // startActivityAuth()

        } else {

            //finishAuthenticationWithFailMessage("Canceled")

        }

    }


}