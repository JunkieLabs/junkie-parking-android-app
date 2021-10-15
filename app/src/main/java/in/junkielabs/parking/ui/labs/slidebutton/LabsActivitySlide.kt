package `in`.junkielabs.parking.ui.labs.slidebutton

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLabsSlideBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LabsActivitySlide : ActivityBase() {
    private lateinit var vBinding: ActivityLabsSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityLabsSlideBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
//        setContentView(R.layout.activity_labs_slide_button)
    }
}