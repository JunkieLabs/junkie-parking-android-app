package `in`.junkielabs.parking

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.firebase.functions.controller.FbFunctionTestController
import `in`.junkielabs.parking.databinding.ActivityMainBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.common.scanner.ActivityQrScanner
import `in`.junkielabs.parking.ui.components.home.ActivityHome
import `in`.junkielabs.parking.ui.components.launcher.ActivityLauncher
import `in`.junkielabs.parking.ui.components.onboard.ActivityOnboard
import `in`.junkielabs.parking.ui.components.wait.ActivityWait
import `in`.junkielabs.parking.ui.components.walkthrough.ActivityWalkThrough
import `in`.junkielabs.parking.ui.labs.slidebutton.LabsActivitySlide
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.jetbrains.anko.info


@AndroidEntryPoint
class MainActivity : ActivityBase() {
    private lateinit var binding: ActivityMainBinding
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var fbFunctionTestController : FbFunctionTestController = FbFunctionTestController()

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
        binding.activityMainFunctionBtn.setOnClickListener {
            //            startActivity<ActivityAuth>()
            testFunction()
        }

        binding.activityMainLauncherBtn.setOnClickListener {
            //            startActivity<ActivityAuth>()
            var i = Intent(this, ActivityLauncher::class.java)
            startActivity(i)
        }

        binding.activityMainWalkBtn.setOnClickListener {
            var i = Intent(this, ActivityWalkThrough::class.java)
            startActivity(i)

        }

        binding.activityMainOnboardBtn.setOnClickListener {
            var i = Intent(this, ActivityOnboard::class.java)
            startActivity(i)

        }

        binding.activityMainWaitBtn.setOnClickListener {
            var i = Intent(this, ActivityWait::class.java)
            startActivity(i)
        }

        binding.activityMainHomeBtn.setOnClickListener {
            var i = Intent(this, ActivityHome::class.java)
            startActivity(i)
        }

        binding.activityMainScannerBtn.setOnClickListener {
            var i = Intent(this, ActivityQrScanner::class.java)
            startActivity(i)
        }

        binding.activityMainSlideBtn.setOnClickListener {
            var i = Intent(this, LabsActivitySlide::class.java)
            startActivity(i)
        }

    }

    private fun testFunction() {

        scope.launch {
            var result = fbFunctionTestController.test("dee");
            info { "result: $result" }
        }
    }


}