package `in`.junkielabs.parking.ui.components.home

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityHomeBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.utils.UtilTheme
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.asdev.phoneedittext.helper.check
import com.asdev.phoneedittext.helper.countryCode
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info

@AndroidEntryPoint
class ActivityHome : ActivityBase() {
    private lateinit var vBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityHomeBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        setRoundedCorner()
        vBinding.wheelerItemBike.wheelerBike.setOnCheckedChangeListener { card, isChecked -> vBinding.wheelerItemBike.wheelerBikeText.isChecked = isChecked }
        vBinding.wheelerItemBike.wheelerBike.isChecked = true
//        vBinding.wheelerItemBike.wheelerBikeText.isChecked = true
        if (!check().countryCodeExisting(vBinding.homeInputPhone)) {
            val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager

            val countryCodeValue = tm.networkCountryIso

            var dialCode = "+" + countryCode().getDialCode(countryCodeValue)
            vBinding.homeInputLayoutPhone.prefixText = dialCode
        }

    }

    override fun getStatusBarColor(): Int {
        info { "getStatusBarColor 1" }
        return ContextCompat.getColor(this, R.color.colorAccent)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }

    private fun setRoundedCorner(){
        val bottomBarBackground  =  vBinding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(RoundedCornerTreatment()).setTopLeftCornerSize(RelativeCornerSize(0.5f))
            .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(
                RelativeCornerSize(
                    0.5f
                )
            )
            .build()
    }



}