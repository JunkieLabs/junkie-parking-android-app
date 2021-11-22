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
import `in`.junkielabs.parking.utils.UtilAnimation
import `in`.junkielabs.parking.utils.UtilRegex
import `in`.junkielabs.parking.utils.UtilTheme
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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
    private var _mKeyBoardOpen: Boolean = false
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
            _mKeyBoardOpen = false
            vBinding.bottomAppBar.hideOnScroll = true
        } else {

            Handler(Looper.getMainLooper()).postDelayed({
                _mKeyBoardOpen = true

                vBinding.bottomAppBar.hideOnScroll = false
                hideBottomTab()
            }, 500)

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
            var currentState = vBinding.checkinoutSlide.checkinoutSlideMotionLayout.currentState
            if (currentState == vBinding.checkinoutSlide.checkinoutSlideMotionLayout.startState) {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToEnd {
                    revealProgress();
                }

            } else {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToStart()

            }
        }


        attachKeyboardListeners()


    }

    private fun revealProgress() {
        var x =
            vBinding.checkinoutSlide.root.x.toInt() + vBinding.checkinoutSlide.root.width.toInt() - vBinding.checkinoutSlide.checkinoutSlideButton.width / 2
        var y =
            vBinding.checkinoutSlide.root.y.toInt() + vBinding.checkinoutSlide.checkinoutSlideButton.height / 2
        UtilAnimation.revealView(
            vBinding.frameProgress.frameProgress,
            x,
            y,
            vBinding.coordinatorLayout.height,
            vBinding.coordinatorLayout.width,
            object : AnimatorListenerAdapter() {


            })
        vBinding.checkinoutSlide.root.animate().alpha(0F).start()
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
        vBinding.homeInputVehicleNumber.filters = arrayOf(
            InputFilter.AllCaps()
//            ,
//            InputFilter.LengthFilter(ParkingConstants.Vehicle.INPUT_FORMAT.length)
        )
        vBinding.homeInputVehicleNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                vBinding.homeInputVehicleNumber.removeTextChangedListener(this)
                var matches =
                    UtilRegex.getInputBoxes(ParkingConstants.Vehicle.INPUT_FORMAT, s.toString())

                var resultText = matches.joinToString("")
                Log.d("ActivityHome:", "setupForm: ${s.toString().length} $s $matches $resultText")
                Log.d(
                    "ActivityHome: ",
                    "aftertextchanged: ${s.toString().length} ${vBinding.homeInputVehicleNumber.selectionEnd}"
                )
                if (resultText != s.toString()) {
//                    s?.clear()


/*                    s?.replace(0, vBinding.homeInputVehicleNumber.selectionEnd, resultText)*/

                    if (s.toString().length > ParkingConstants.Vehicle.INPUT_FORMAT.length) {
                        s?.delete(ParkingConstants.Vehicle.INPUT_FORMAT.length, s.toString().length)
                        vBinding.homeInputVehicleNumber.setText(s)
                        vBinding.homeInputVehicleNumber.setSelection(s.toString().length)
                    } else {
                        vBinding.homeInputVehicleNumber.setText(resultText)
                        vBinding.homeInputVehicleNumber.setSelection(resultText.length)

                    }
//                vBinding.homeInputVehicleNumber.setText(resultText)

                }

                vBinding.homeInputVehicleNumber.addTextChangedListener(this)
            }

        })
        /* vBinding.homeInputVehicleNumber.addTextChangedListener { text ->

         }*/

    }


}