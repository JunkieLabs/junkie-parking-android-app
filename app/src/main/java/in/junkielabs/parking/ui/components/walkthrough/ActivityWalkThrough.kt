package `in`.junkielabs.parking.ui.components.walkthrough

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLauncherBinding
import `in`.junkielabs.parking.databinding.ActivityWalkThroughBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityWalkThrough : ActivityBase() {
    private lateinit var binding: ActivityWalkThroughBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkThroughBinding.inflate(layoutInflater)
        setStatusDefault(true)
        setContentView(binding.root)
    }
}