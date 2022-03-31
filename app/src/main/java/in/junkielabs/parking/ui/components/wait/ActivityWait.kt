package `in`.junkielabs.parking.ui.components.wait

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityWaitBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import android.os.Bundle
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

class ActivityWait : ActivityBase() {
    private lateinit var vBinding: ActivityWaitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityWaitBinding.inflate(layoutInflater)
        setStatusDefault(true)
        setContentView(vBinding.root)
        Glide.with(vBinding.activityOnboardImage)
            .load(R.drawable.pic_please_wait)
            .thumbnail(0.5f)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //                    .transform(new CircleTransform(mContext))
            //                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(vBinding.activityOnboardImage)

    }


}