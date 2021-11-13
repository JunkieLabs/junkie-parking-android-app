package `in`.junkielabs.parking.ui.components.home

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ActivityHomeBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModel
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModelFactory
import `in`.junkielabs.parking.ui.components.onboard.viewmodel.OnboardViewModel
import `in`.junkielabs.parking.ui.components.onboard.viewmodel.OnboardViewModelFactory
import `in`.junkielabs.parking.utils.UtilRegex
import `in`.junkielabs.parking.utils.UtilTheme
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.asdev.phoneedittext.helper.check
import com.asdev.phoneedittext.helper.countryCode
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.shape.RoundedCornerTreatment
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.info
import java.util.*

import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener


@AndroidEntryPoint
class ActivityHome : ActivityBase() {
    private lateinit var vBinding: ActivityHomeBinding

    private val mViewModel: HomeViewModel by viewModels(
        factoryProducer = { HomeViewModelFactory(ApplicationMy.instance) }
    )

    private var mKeyboardListenersAttached = false
    private var mKeyboardListener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightRoot = vBinding.root.rootView.getHeight()
        val heightLayout = vBinding.root.getHeight()
        val heightDiff: Int = heightRoot - heightLayout// - vBinding.getHeight()
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top

        if (heightDiff <= contentViewTop) {
            showBottomTab()

        } else {

            Handler(Looper.getMainLooper()).postDelayed({ hideBottomTab() }, 500)

        }

        Log.i(
            "ActivityHome: mKeyboardListener:",
            "$heightRoot $heightLayout  $heightDiff $contentViewTop"
        )


    }

    private fun showBottomTab() {
        vBinding.bottomAppBar.performShow()
        vBinding.activityHomeFab.show()
    }

    private fun hideBottomTab() {
        vBinding.bottomAppBar.performHide()
        vBinding.activityHomeFab.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityHomeBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        setRoundedCorner()


        setupView()
        setupViewModel()

        setupForm()

    }

    override fun getStatusBarColor(): Int {
        info { "getStatusBarColor 1" }
        return ContextCompat.getColor(this, R.color.colorAccent)
    }

    override fun setStatusBar(color: Int) {
        UtilTheme.setDarkStatusBar(window.decorView, this, color)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mKeyboardListenersAttached) {
            vBinding.root.viewTreeObserver.removeOnGlobalLayoutListener(mKeyboardListener);
        }
    }

    private fun setupView() {
        /*vBinding.wheelerItemBike.wheelerBike.setOnCheckedChangeListener { card, isChecked -> mViewModel.setWheelerChecked(ParkingConstants.Wheeler.TYPE_BIKE) }

        vBinding.wheelerItemCar.wheelerCar.setOnCheckedChangeListener { card, isChecked ->
            mViewModel.setWheelerChecked(ParkingConstants.Wheeler.TYPE_CAR) }
*/
        vBinding.wheelerItemBike.wheelerBike.setOnClickListener {
            mViewModel.setWheelerChecked(ParkingConstants.Wheeler.TYPE_BIKE)
        }

        vBinding.wheelerItemCar.wheelerCar.setOnClickListener {
            mViewModel.setWheelerChecked(ParkingConstants.Wheeler.TYPE_CAR)
        }

//        vBinding.wheelerItemBike.wheelerBikeText.isChecked = true
        if (!check().countryCodeExisting(vBinding.homeInputPhone)) {
            val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager

            val countryCodeValue = tm.networkCountryIso

            var dialCode = "+" + countryCode().getDialCode(countryCodeValue)
            vBinding.homeInputLayoutPhone.prefixText = dialCode
        }
        vBinding.checkinoutSlide.checkinoutSlideButton.setOnClickListener {
            info { "clciked" }
            var ewew = vBinding.checkinoutSlide.checkinoutSlideMotionLayout.currentState
            if (ewew == vBinding.checkinoutSlide.checkinoutSlideMotionLayout.startState) {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToEnd()

            } else {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToStart()

            }
        }

        attachKeyboardListeners()


    }

    protected fun attachKeyboardListeners() {
        if (mKeyboardListenersAttached) {
            return
        }

        vBinding.root.viewTreeObserver.addOnGlobalLayoutListener(mKeyboardListener)
        mKeyboardListenersAttached = true
    }

    private fun setupViewModel() {
        mViewModel.initData()

        mViewModel.bParkingArea.observe(this, {
            vBinding.activityHomeAreaName.text =
                it.name?.replace("_", " ")?.replaceFirstChar { it1 -> it1.titlecase() }
        })

        mViewModel.bWheelerBike.observe(this, {
            if (it != null) {
                vBinding.wheelerItemBike.root.visibility = View.VISIBLE
            } else {
                vBinding.wheelerItemBike.root.visibility = View.GONE
            }
        })

        mViewModel.bWheelerCar.observe(this, {
            if (it != null) {
                vBinding.wheelerItemCar.root.visibility = View.VISIBLE
            } else {
                vBinding.wheelerItemCar.root.visibility = View.GONE
            }
        })

        mViewModel.bWheelerType.observe(this, {

            Log.i("ActivityHome", "bWheelerType $it")
            if (it == ParkingConstants.Wheeler.TYPE_BIKE) {
                vBinding.wheelerItemBike.wheelerBike.isChecked = true
                vBinding.wheelerItemBike.wheelerBikeText.isChecked = true

                vBinding.wheelerItemCar.wheelerCar.isChecked = false
                vBinding.wheelerItemCar.wheelerCarText.isChecked = true
            } else if (it == ParkingConstants.Wheeler.TYPE_CAR) {
                vBinding.wheelerItemBike.wheelerBike.isChecked = false
                vBinding.wheelerItemBike.wheelerBikeText.isChecked = false

                vBinding.wheelerItemCar.wheelerCar.isChecked = true
                vBinding.wheelerItemCar.wheelerCarText.isChecked = false
            }
        })


    }

    private fun setRoundedCorner() {
        val bottomBarBackground = vBinding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel =
            bottomBarBackground.shapeAppearanceModel.toBuilder()
                .setTopLeftCorner(RoundedCornerTreatment())
                .setTopLeftCornerSize(RelativeCornerSize(0.5f))
                .setTopRightCorner(RoundedCornerTreatment()).setTopRightCornerSize(
                    RelativeCornerSize(
                        0.5f
                    )
                )
                .build()
    }

    private fun setupForm() {
        vBinding.homeInputVehicleNumber.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(ParkingConstants.Vehicle.INPUT_FORMAT.length))
        vBinding.homeInputVehicleNumber.addTextChangedListener { text ->
            var matches =
                UtilRegex.getInputBoxes(ParkingConstants.Vehicle.INPUT_FORMAT, text.toString())

            var resultText = matches.joinToString("")
            Log.d("ActivityHome:", "setupForm: ${text.toString().length} $text $matches $resultText")
            if (resultText != text.toString()) {
                text?.clear()

                text?.replace(0, text.toString().length, resultText)
                vBinding.homeInputVehicleNumber.setSelection(resultText.length)
//                vBinding.homeInputVehicleNumber.setText(resultText)

            }
        }

    }


}