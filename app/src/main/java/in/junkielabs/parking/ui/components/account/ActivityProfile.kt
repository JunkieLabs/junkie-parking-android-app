package `in`.junkielabs.parking.ui.components.account

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.databinding.ActivityProfileBinding
import `in`.junkielabs.parking.databinding.ActivityReportBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.launcher.ActivityLauncher
import `in`.junkielabs.parking.ui.components.thanks.ActivityThanks
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class ActivityProfile : ActivityBase() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setStatusDefault(true)
        setContentView(binding.root)
        initToolbar(R.drawable.ic_arrow_back, binding.toolbar)
        toolbarTitle = ""
        bindData()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun bindData() {
        var guard = ApplicationMy.instance.appAccount.getGuard()

        if(guard?.name!=null){
            binding.activityProfileName.text = guard.name
        }

        var parkingArea = ApplicationMy.instance.appAccount.getParkingArea()

        if(parkingArea?.name!=null){
            binding.activityProfileParkingAreaName.text = parkingArea.name
        }

        binding.activityProfileActionLogout.setOnClickListener {
            logout()
        }

        binding.activityProfileActionAboutus.setOnClickListener {
            var i = Intent(this, ActivityThanks::class.java)
            startActivity(i)
        }
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        ApplicationMy.instance.appAccount.reset()
        navigateToActivityLauncher()

    }

    private fun navigateToActivityLauncher() {
        val i = Intent(this, ActivityLauncher::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(i)
        finish()
    }


}