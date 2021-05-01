package `in`.junkielabs.parking.ui.components.onboard

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.databinding.ActivityOnboardBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.utils.UtilTheme
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info

@AndroidEntryPoint
class ActivityOnboard : ActivityBase() {
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
    }

    override fun getStatusBarColor(): Int {
        info { "getStatusBarColor 1" }
        return ContextCompat.getColor(this, R.color.colorAccentDark)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }



}