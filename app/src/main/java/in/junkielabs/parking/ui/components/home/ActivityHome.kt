package `in`.junkielabs.parking.ui.components.home

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ActivityHomeBinding
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.common.checkinout.dialogs.CheckInDialog
import `in`.junkielabs.parking.ui.common.checkinout.dialogs.CheckOutDialog
import `in`.junkielabs.parking.ui.components.account.ActivityProfile
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModel
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModelFactory
import `in`.junkielabs.parking.ui.components.report.ActivityReport
import `in`.junkielabs.parking.utils.UtilAnimation
import `in`.junkielabs.parking.utils.UtilRegex
import `in`.junkielabs.parking.utils.UtilTheme
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
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
import java.util.*

import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.snackbar.Snackbar


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
//        info { "getStatusBarColor 1" }
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
            Log.d("ActivityHome", "checkinoutSlide clciked")
            var currentState = vBinding.checkinoutSlide.checkinoutSlideMotionLayout.currentState
            if (currentState == vBinding.checkinoutSlide.checkinoutSlideMotionLayout.startState) {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToEnd {

                    if (vBinding.checkinoutSlide.checkinoutSlideMotionLayout.currentState == vBinding.checkinoutSlide.checkinoutSlideMotionLayout.endState) {
                        revealProgress();
                    }
                }

            } else {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.transitionToStart()

            }
        }

        vBinding.activityHomeActionReport.setOnClickListener {
            navigateToActivityReport()
        }

        vBinding.activityHomeActionProfile.setOnClickListener {
            navigateToActivityProfile()
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

        mViewModel.bFormIsValid.observe(this, {

            Log.d("ActivityHome", "bFormIsValid: $it")
            if (it) {
                showSubmitBtn()
            } else {
                hideSubmitBtn()
            }
        })

        mViewModel.mEventErrorMessage.observe(this, LiveDataObserver { t ->
            Snackbar.make(
                vBinding.coordinatorLayout, t,
                Snackbar.LENGTH_LONG
            ).show()
            showSubmitBtn()
            hideProgress()
        })

        mViewModel.mEventCheckInOut.observe(this, LiveDataObserver { t ->
//            show
            showSubmitBtn()
            hideProgress()
            dialogCheckInOut(t)
        })

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


    private fun navigateToActivityReport() {
//        info{"navigateToActivityPaperFlow: $pepCourseId"}
        val i = Intent(this, ActivityReport::class.java)
        startActivity(i)
    }


    private fun navigateToActivityProfile() {
//        info{"navigateToActivityPaperFlow: $pepCourseId"}
        val i = Intent(this, ActivityProfile::class.java)
        startActivity(i)
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
                mViewModel.formVehicleNumber(resultText)
                vBinding.homeInputVehicleNumber.addTextChangedListener(this)
            }

        })

        vBinding.homeInputPhone.addTextChangedListener(object : TextWatcherAdapter() {
            @SuppressLint("RestrictedApi")
            override fun afterTextChanged(s: Editable) {
                super.afterTextChanged(s)
                mViewModel.formPhoneNumber(s.toString())
            }

        })
        /* vBinding.homeInputVehicleNumber.addTextChangedListener { text ->

         }*/

    }

    /* ****************************************************************************
     *                                Button
     */

    private fun hideSubmitBtn() {

        Log.d("ActivityHome: ", "hideSubmitBtn: ")
        if (vBinding.checkinoutSlide.root.visibility == View.VISIBLE) {
            vBinding.checkinoutSlide.root.animate().setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    vBinding.checkinoutSlide.root.visibility = View.GONE
                }
            }).alpha(0F).start()
        }


    }


    private fun showSubmitBtn() {

        Log.d("ActivityHome: ", "showSubmitBtn: ")
        if (vBinding.checkinoutSlide.root.visibility == View.GONE) {
            vBinding.checkinoutSlide.root.alpha = 0.0f
            vBinding.checkinoutSlide.root.visibility = View.VISIBLE
            vBinding.checkinoutSlide.root.animate().setListener(object : AnimatorListenerAdapter() {


                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)

                    vBinding.checkinoutSlide.root.alpha = 0.0f
                    vBinding.checkinoutSlide.root.visibility = View.GONE
                }
            }).alpha(1F).start()
            var currentState = vBinding.checkinoutSlide.checkinoutSlideMotionLayout.currentState
            if (currentState != vBinding.checkinoutSlide.checkinoutSlideMotionLayout.startState) {
                vBinding.checkinoutSlide.checkinoutSlideMotionLayout.jumpToState(vBinding.checkinoutSlide.checkinoutSlideMotionLayout.startState)
            }
        }


    }

    /* *******************************************************************************
     *                                  progress
     */

    private fun hideProgress() {
        vBinding.frameProgress.root.visibility = View.GONE

    }

    private fun showProgress() {
        vBinding.frameProgress.root.visibility = View.VISIBLE
    }

    private fun revealProgress() {

        Log.e("ActivityHome", "revealProgress")
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
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    mViewModel.onSubmit()
                }


            })

        hideSubmitBtn()
    }


    /* *******************************************************************************
     *                                   dialog
     */

    private fun dialogCheckInOut(checkInOut: ParamCheckInOut) {
        if (checkInOut.outTimestamp != null) {
            dialogCheckOut(checkInOut)
        } else {
            dialogCheckIn(checkInOut)
        }
    }

    private fun dialogCheckIn(checkInOut: ParamCheckInOut) {
        val b = Bundle()
        b.putParcelable(CheckOutDialog.B_ARG_CHECKINOUT, checkInOut)

        val alert = CheckInDialog.newInstance(
            101,
            b
        )

        alert.isCancelable = true

        alert.show(
            supportFragmentManager,
            CheckOutDialog::class.java.simpleName
        )
    }

    private fun dialogCheckOut(checkInOut: ParamCheckInOut) {
        val b = Bundle()
        b.putParcelable(CheckOutDialog.B_ARG_CHECKINOUT, checkInOut)

        val alert = CheckOutDialog.newInstance(
            101,
            b
        )

        alert.isCancelable = true

        alert.show(
            supportFragmentManager,
            CheckOutDialog::class.java.simpleName
        )
    }

}