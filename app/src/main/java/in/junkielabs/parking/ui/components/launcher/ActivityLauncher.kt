package `in`.junkielabs.parking.ui.components.launcher

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.utils.UtilColor
import `in`.junkielabs.parking.utils.UtilTheme
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info


@AndroidEntryPoint
class ActivityLauncher : ActivityBase() {
    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(binding.root)
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
}