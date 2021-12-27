package `in`.junkielabs.parking.ui.common.scanner

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.models.vehicle.ParamVehicle
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.ActivityQrScannerBinding
import `in`.junkielabs.parking.tools.livedata.LiveDataObserver
import `in`.junkielabs.parking.ui.base.ActivityBase
import `in`.junkielabs.parking.ui.common.scanner.viewmodel.QrScannerViewModel
import `in`.junkielabs.parking.ui.common.scanner.viewmodel.QrScannerViewModelFactory
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModel
import `in`.junkielabs.parking.ui.components.home.viewmodel.HomeViewModelFactory
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Size
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutionException


// https://learntodroid.com/how-to-create-a-qr-code-scanner-app-in-android/
// https://www.nomtek.com/blog/motionlayout
// https://github.com/journeyapps/zxing-android-embedded/blob/master/zxing-android-embedded/src/com/journeyapps/barcodescanner/ViewfinderView.java

@AndroidEntryPoint
class ActivityQrScanner : ActivityBase() {

    companion object{

        const val STATE_NONE = 0
            const val STATE_DEFAULT = 100
            const val STATE_FOUND = 101
            const val STATE_ERROR = 102
        val B_RESULT_ARG_VEHICLE: String =
            ActivityQrScanner::class.java.simpleName + ".arg_vehicle"


    }

    private var mCurrentViewState: Int =  STATE_NONE
    private val mViewModel: QrScannerViewModel by viewModels(
        factoryProducer = { QrScannerViewModelFactory(ApplicationMy.instance) }
    )

    private var mImageAnalyzer: QrCodeImageAnalyzer? = null
    private lateinit var mCameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var vBinding: ActivityQrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityQrScannerBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        initToolbar(R.drawable.ic_round_arrow_back_24_white, vBinding.toolbar)
        toolbarTitle = ""
        mCameraProviderFuture = ProcessCameraProvider.getInstance(this);
        requestPermission()



        setupView()
        setupViewModel()

    }




    override fun getStatusBarColor(): Int {

//        return ColorUtils.blendARGB(Color.parseColor("#FFFFFF"), Color.BLACK, 0.12F)
        return ContextCompat.getColor(this, R.color.md_grey_900)
//        return UtilColor.getColorForAlpha(Color.parseColor("#FFFFFF"), 0.12F)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupView() {
        vBinding.activityQrScannerIndicator.setFrameCornersRadius(8)
        vBinding.activityQrScannerIndicator.setFrameColor(Color.RED)
        vBinding.activityQrScannerIndicator.setFrameCornersSize(40)
        vBinding.qrScannerError.qrScannerErrorBtn.setOnClickListener {
            bindViewState(STATE_DEFAULT)
        }

        vBinding.qrScannerVehicle.qrScannerOkBtn.setOnClickListener {
           if(mViewModel.mVehicle!=null){
               finishSuccess(mViewModel.mVehicle!!)
           }
        }
        bindViewState(STATE_DEFAULT)
    }

    private fun finishSuccess(mVehicle: ParamVehicle) {
        var i   = Intent()
        i.putExtra(B_RESULT_ARG_VEHICLE, mVehicle)

        setResult(RESULT_OK, i)
        finish()
    }

    private fun setupViewModel() {
        mViewModel.mEventErrorMessage.observe(this, LiveDataObserver { t ->

            bindViewState(STATE_ERROR)

            hideProgress()
            showErrorMessage(t)
        })

        mViewModel.mEventVehicle.observe(this, LiveDataObserver { t ->
//            show

            bindViewState(STATE_FOUND)
            hideProgress()
            bindVehicle(t)
        })

        mViewModel.mIsInProgress.observe(this, Observer {
            if(it){
                showProgress()
            }else{
                hideProgress()
            }
        })
    }

    private fun bindVehicle(vehicle: ParamVehicle) {
        vBinding.qrScannerVehicle.qrScannerVehicleNumber.text = vehicle.number
        vBinding.qrScannerVehicle.qrScannerVehicleType.text = vehicle.number

        if (vehicle.wheeler.type == ParkingConstants.Wheeler.TYPE_BIKE) {
            vBinding.qrScannerVehicle.qrScannerVehicleImage.setImageDrawable(
                ContextCompat.getDrawable(
                   this,
                    R.drawable.ic_scooter
                )
            )
            vBinding.qrScannerVehicle.qrScannerVehicleType.text = "2 wheeler"
        } else if (vehicle.wheeler.type == ParkingConstants.Wheeler.TYPE_CAR) {
            vBinding.qrScannerVehicle.qrScannerVehicleImage.setImageDrawable(
                ContextCompat.getDrawable(
                   this,
                    R.drawable.ic_report_car
                )
            )

            vBinding.qrScannerVehicle.qrScannerVehicleType.text = "4 wheeler"
        }

    }

    private fun showErrorMessage(message: String) {
        vBinding.qrScannerError.qrScannerErrorText.text = message

    }

    private fun bindViewState(state: Int) {

        if(mCurrentViewState == state)return
        mCurrentViewState = state

        if(mCurrentViewState == STATE_DEFAULT){
            resumeCameraAnalyze()
            vBinding.qrScannerError.root.visibility = View.GONE
            vBinding.qrScannerVehicle.root.visibility = View.GONE


        }else if(mCurrentViewState == STATE_FOUND){
            vBinding.qrScannerError.root.visibility = View.GONE
            vBinding.qrScannerVehicle.root.visibility = View.VISIBLE

        }else if(mCurrentViewState == STATE_ERROR){
            vBinding.qrScannerError.root.visibility = View.VISIBLE
            vBinding.qrScannerVehicle.root.visibility = View.GONE

        }
    }

    private fun hideProgress() {
//        vBinding.frameProgress.root.visibility = View.GONE
        vBinding.frameProgress.root.visibility = View.GONE

    }

    private fun showProgress() {
//        vBinding.frameProgress.root.visibility = View.VISIBLE
        vBinding.frameProgress.root.visibility = View.VISIBLE
    }

    /* ***************************************************************************************
     *
     */

    private fun startCamera() {
        mCameraProviderFuture.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = mCameraProviderFuture.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(this, "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error starting camera " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        vBinding.activityQrScannerPreview.implementationMode =
            PreviewView.ImplementationMode.COMPATIBLE
        val preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(vBinding.activityQrScannerPreview.surfaceProvider)
        mImageAnalyzer =
            QrCodeImageAnalyzer(object : QrCodeImageAnalyzer.QrCodeImageAnalyzerListener {
                override fun onQRCodeFound(text: String) {
//                    qrCode = _qrCode
                    pauseCameraAnalyze()
//                    mImageAnalyzer?.shouldAnalyse(false)
                    mViewModel.onQrCode(text)
//                    info { "QrCodeImageAnalyzer $text" }
//                    vBinding.activityQrScannerIndicator.pause()
//                    toast(text)
//                    qrCodeFoundButton.setVisibility(View.VISIBLE)
                }

                override fun qrCodeNotFound() {


//                    info { "qrCodeNotFound" }
//                    qrCodeFoundButton.setVisibility(View.INVISIBLE)
                }
            })

        val imageAnalysis = ImageAnalysis.Builder()
//            .setTargetResolution(Size(1280, 720))

            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            mImageAnalyzer!!
        )


        val camera: Camera =
            cameraProvider.bindToLifecycle(
                (this as LifecycleOwner),
                cameraSelector,
                imageAnalysis,
                preview
            )
    }

    fun pauseCameraAnalyze(){
        mImageAnalyzer?.shouldAnalyse(false)
        vBinding.activityQrScannerIndicator.pause()
    }

    fun resumeCameraAnalyze(){
        mImageAnalyzer?.shouldAnalyse(true)
//        info { "QrCodeImageAnalyzer $text" }
        vBinding.activityQrScannerIndicator.resume()
    }

    /* ********************************************************************************************
     *                                          Permission
     */

    private fun requestPermission() {

        val snackbarPermission =
            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(
                vBinding.frameContent,
                "All those permissions are needed for this section"
            )
                .withOpenSettingsButton("Settings")
                .withDuration(4000)
                .build()

        val appPermissionsListener = object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {


                if (p0 != null) {
                    if (p0.areAllPermissionsGranted()) {
                        Snackbar.make(vBinding.frameContent, "Permission Granted !!", 4000).show()
                        startCamera()
                    }
                    for (response in p0.grantedPermissionResponses) {
                        //                    p0.showPermissionGranted(response.permissionName)
                    }


                    var showPermissionRational = false
                    for (response in p0.deniedPermissionResponses) {
                        /*p0.showPermissionDenied(
                            response.permissionName,
                            response.isPermanentlyDenied
                        )*/
                        if (!response.isPermanentlyDenied) {
                            showPermissionRational = true
                        }
                    }

                    if (showPermissionRational) showPermissionRationale()
                }


            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {

                p1?.let {
                    p1.continuePermissionRequest()
//                    showPermissionRationale(it)

                }

            }

        }

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
            .withListener(
                CompositeMultiplePermissionsListener(appPermissionsListener, snackbarPermission)
            )
            .withErrorListener {
                Snackbar.make(vBinding.frameContent, it.toString(), 4000).show()
            }
            .check();
    }


    private fun showPermissionRationale() {

        AlertDialog.Builder(this).setTitle("We need this permission")
            .setMessage("This permission is needed for doing some fancy stuff so please, allow it!")
            .setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ ->
                dialog.dismiss()
//                token.cancelPermissionRequest()
            }
            .setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                requestPermission()
//                info { token }
//                token.continuePermissionRequest()
            }

            .setOnDismissListener {
//                token.cancelPermissionRequest()
            }
            .show()

    }


}