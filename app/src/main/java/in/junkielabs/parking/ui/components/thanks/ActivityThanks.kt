package `in`.junkielabs.parking.ui.components.thanks

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityHomeBinding
import `in`.junkielabs.parking.databinding.ActivityThanksBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.utils.UtilTheme
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat

class ActivityThanks : ActivityBase() {
    private lateinit var vBinding: ActivityThanksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityThanksBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        initToolbar(R.drawable.ic_round_arrow_back_24_white, vBinding.toolbar)
        toolbarTitle = ""

        vBinding.activityThanksBtnGithub.setOnClickListener {
            openGithub()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    override fun getStatusBarColor(): Int {
//        info { "getStatusBarColor 1" }
        return ContextCompat.getColor(this, R.color.colorAccent)
    }

    //

    /*override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }*/

    private fun openGithub() {
        val uri = Uri.parse("https://github.com/JunkieLabs/junkie-parking-android-app")
        val likeIng =
            Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.github.android")

        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/JunkieLabs/junkie-parking-android-app")
                )
            )
        }
    }
}