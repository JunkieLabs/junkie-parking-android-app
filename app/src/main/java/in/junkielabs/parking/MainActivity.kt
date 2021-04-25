package `in`.junkielabs.parking

import `in`.junkielabs.parking.databinding.ActivityMainBinding
import `in`.junkielabs.parking.ui.components.launcher.ActivityLauncher
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.startActivity


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

    private fun initButton() {

        binding.activityMainLauncherBtn.setOnClickListener {
            //            startActivity<ActivityAuth>()
            startLauncher()
        }



    }
    fun startLauncher(){
        startActivity<ActivityLauncher>()

    }
}