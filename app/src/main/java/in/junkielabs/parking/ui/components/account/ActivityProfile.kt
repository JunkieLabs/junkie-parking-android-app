package `in`.junkielabs.parking.ui.components.account

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.ui.base.ActivityBase
import android.os.Bundle

class ActivityProfile : ActivityBase() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}