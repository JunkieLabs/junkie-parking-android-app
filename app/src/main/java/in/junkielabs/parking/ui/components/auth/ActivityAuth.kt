package `in`.junkielabs.parking.ui.components.auth

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.common.progress.FrameProgressDialog
import `in`.junkielabs.parking.ui.components.auth.google.ActivityAuthGoogle
import `in`.junkielabs.parking.ui.components.auth.viewmodel.AuthViewModel
import `in`.junkielabs.parking.ui.components.auth.viewmodel.AuthViewModelFactory
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

/**
 * Created by Niraj on 22-10-2021.
 */
class ActivityAuth : ActivityBase(){
    private val mViewModel: AuthViewModel by viewModels(
        factoryProducer = { AuthViewModelFactory(ApplicationMy.instance) }

//    ownerProducer = { return@viewModels this }
    )

    protected var mResultBundle: Bundle? = null

    var mActivityResultGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            info{"mActivityResultSignin: ${result.data}"}
            onAuthenticationResult(result.data, result.resultCode)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        val i = intent

        val action = i.getStringExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION)
            ?: AccountConstants.Account.ACTION_SIGNIN

        mViewModel.initData(action)

        mViewModel.mEventAccountAction.observe(this,
            LiveDataObserver { t ->

                onAccountActionRequest(t)

            }
        )

        mViewModel.mEventProgress.observe(this,
            LiveDataObserver { t ->
                if (t) {
                    openProgress()
                } else {
                    closeProgress()
                }

            }
        )

        mViewModel.mEventIsCreated.observe(this,
            LiveDataObserver { t ->

                if(t){
                    finishAuthenticationWithSuccessMessage("Success Full")
                }else{
                    finishAuthenticationWithFailMessage("Failed to save account")
                }



            }
        )

        mViewModel.mEventLoggedOut.observe(this, LiveDataObserver{
            if(it){
                finishAuthenticationWithSuccessMessage("Successfully Logged Out.")
            }else{
                finishAuthenticationWithFailMessage("Failed to logout")
            }

        })
    }

    private fun onAccountActionRequest(actionRequest: String) {
        when (actionRequest) {

            AccountConstants.Account.ACTION_SIGNIN -> {
                startActivitySignin()
            }

            AccountConstants.Account.ACTION_REAUTH -> {


                mViewModel.reAuthenticate()
            }

            AccountConstants.Account.ACTION_REMOVE -> {
                startActivityRemove()
//                mViewModel.logout()
            }


            else -> { }// TODO remove account
        }

    }

    private fun startActivitySignin() {
        val intent = Intent(this, ActivityAuthGoogle::class.java)
        intent.putExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION, AccountConstants.Account.ACTION_SIGNIN)

        mActivityResultGoogle.launch(intent)
    }

    private fun startActivityRemove() {
        val intent = Intent(this, ActivityAuthGoogle::class.java)
        intent.putExtra(AccountConstants.Account.Arguments.ACCOUNT_ACTION, AccountConstants.Account.ACTION_REMOVE)
        mActivityResultGoogle.launch(intent)
    }


    private fun onAuthenticationResult(data: Intent?, resultCode: Int) {

        if (resultCode == RESULT_OK) {

            var action = mViewModel.getCurrentAction()
            if(action == AccountConstants.Account.ACTION_REMOVE){
                mViewModel.logout()
            }else if(action == AccountConstants.Account.ACTION_SIGNIN){
                mViewModel.onAuthorized()
            }

        } else {

            finishAuthenticationWithFailMessage("Canceled")

        }

    }

    /* ****************************************************************************
   *                                  Progress Dialog
   */

    private fun openProgress() {
        var progress = FrameProgressDialog.newInstance(null);
        progress.show(supportFragmentManager, FrameProgressDialog.TAG)
    }

    private fun closeProgress() {
//        info { "closeProgress: " }
//        FrameProgressDialog.rem
        var fragment = supportFragmentManager.findFragmentByTag(FrameProgressDialog.TAG)

        if (fragment is FrameProgressDialog) {
//            info { "closeProgress: " }
            fragment.dismiss()
        }
    }


    /* ************************************************************************************
     *
     *
     */

    private fun finishAuthenticationWithFailMessage(message: String) {
        val i = Intent()
        intent.extras?.let { i.putExtras(it) }
        i.putExtra(AccountConstants.AccountResult.Arguments.MESSAGE, message)
        i.putExtra(
            AccountConstants.AccountResult.Arguments.CODE,
            AccountConstants.AccountResult.CODE_FAILED
        )
        finishAuthentication(i, Activity.RESULT_CANCELED)
    }

    private fun finishAuthenticationWithSuccessMessage(message: String) {
        val i = Intent()
        intent.extras?.let { i.putExtras(it) }
        i.putExtra(AccountConstants.AccountResult.Arguments.MESSAGE, message)
        i.putExtra(
            AccountConstants.AccountResult.Arguments.CODE,
            AccountConstants.AccountResult.CODE_OK
        )
        finishAuthentication(i, Activity.RESULT_OK)
    }

    private fun finishAuthentication(intent: Intent, resultCode: Int) {

        intent.putExtra(
            AccountConstants.Account.Arguments.ACCOUNT_ACTION,
            mViewModel.getCurrentAction()
        )

        if (intent.extras != null)
            setAccountAuthenticatorResult(intent.extras!!)
        setResult(resultCode, intent)
        finish()
    }

    private fun setAccountAuthenticatorResult(result: Bundle) {
        mResultBundle = result
    }

    /* *************************************************************************************
     *                                      finish work
     */



}