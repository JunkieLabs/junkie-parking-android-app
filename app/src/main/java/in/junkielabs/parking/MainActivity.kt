package `in`.junkielabs.parking

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.databinding.ActivityMainBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.home.ActivityHome
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class MainActivity : ActivityBase() {
    private lateinit var binding: ActivityMainBinding
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow()
//        }

        initButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initButton() {

        binding.activityMainLogoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            ApplicationMy.instance.appAccount.reset()

        }


        binding.activityMainAppBtn.setOnClickListener {
            var i = Intent(this, ActivityHome::class.java)
            startActivity(i)
        }


    }


    /* ******************************************************************************
 *                                       Alert Error
 */





}