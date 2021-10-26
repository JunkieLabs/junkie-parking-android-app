package `in`.junkielabs.parking.ui.components.auth.google

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.components.account.AccountConstants
import `in`.junkielabs.parking.databinding.ActivityAuthGoogleBinding
import `in`.junkielabs.parking.ui.UiConstants
import `in`.junkielabs.parking.ui.base.ActivityBase
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Created by Niraj on 22-10-2021.
 */
class ActivityAuthGoogle : ActivityBase() {

    private var mAuth: FirebaseAuth? = null

    private var mAuthAction: String = AccountConstants.Account.ACTION_SIGNIN


    private lateinit var vBinding: ActivityAuthGoogleBinding
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var mActivityResultSignin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onAuthenticationResult(result.data, result.resultCode)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_JunkieParking_Transparent);
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        vBinding = ActivityAuthGoogleBinding.inflate(layoutInflater)
        setContentView(vBinding.root)
//        setContentView(R.layout.activity_auth_google)
        var gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth?.currentUser

        if (intent != null)
            mAuthAction = intent.getStringExtra(
                AccountConstants.Account.Arguments.ACCOUNT_ACTION
            ) ?: AccountConstants.Account.ACTION_SIGNIN
        if (mAuthAction == AccountConstants.Account.ACTION_REMOVE) {
            signOut()
        } else {
            signIn()
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        info("firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        vBinding.frameProgress.root.visibility = View.VISIBLE
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    info("signInWithCredential:success")
                    val user = mAuth?.currentUser
                    updateResult(user)
                } else {
                    // If sign in fails, display a message to the user.
                    warn("signInWithCredential:failure", task.getException())
                    Snackbar.make(
                        findViewById(R.id.frame_progress),
                        "Authentication Failed.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    updateResult(null)
                }

                // [START_EXCLUDE]
                vBinding.frameProgress.root.visibility = View.GONE
                //                        hideProgressDialog()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        mActivityResultSignin.launch(signInIntent)
    }
    // [END signin]

    private fun signOut() {
        // Firebase sign out
        mAuth?.signOut()

        // Google sign out
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            updateResult(null)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        mAuth?.signOut()

        // Google revoke access
        mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener(
            this
        ) { updateResult(null) }
    }

    private fun onAuthenticationResult(data: Intent?, resultCode: Int) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)
            account?.let { firebaseAuthWithGoogle(it) }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            warn("Google sign in failed", e)
            e.printStackTrace()
            // [START_EXCLUDE]
            updateResult(null)
            // [END_EXCLUDE]
        }
    }


    private fun updateResult(user: FirebaseUser?) {
        user?.let { info { "user: $it" } }

        vBinding.frameProgress.root.visibility = View.GONE
        if (mAuthAction == AccountConstants.Account.ACTION_SIGNIN && user != null && user.isEmailVerified) {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
        }else if(mAuthAction == AccountConstants.Account.ACTION_REMOVE){
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)

        }


        finish()

    }

}