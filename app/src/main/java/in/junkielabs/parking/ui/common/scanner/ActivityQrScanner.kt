package `in`.junkielabs.parking.ui.common.scanner

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityQrScannerBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import android.Manifest
import android.os.Bundle
import android.util.Size
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
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
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.concurrent.ExecutionException


// https://learntodroid.com/how-to-create-a-qr-code-scanner-app-in-android/
// https://www.nomtek.com/blog/motionlayout
// https://github.com/journeyapps/zxing-android-embedded/blob/master/zxing-android-embedded/src/com/journeyapps/barcodescanner/ViewfinderView.java

@AndroidEntryPoint
class ActivityQrScanner : ActivityBase() {
    private lateinit var mCameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var vBinding: ActivityQrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityQrScannerBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        initToolbar(R.drawable.ic_arrow_back_boxed, vBinding.toolbar)
        toolbarTitle = ""
        mCameraProviderFuture = ProcessCameraProvider.getInstance(this);
        requestPermission()

    }

    override fun getStatusBarColor(): Int {
        info { "getStatusBarColor 1" }

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
        vBinding.activityQrScannerPreview.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        val preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(vBinding.activityQrScannerPreview.surfaceProvider)


        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            QrCodeImageAnalyzer(object : QrCodeImageAnalyzer.QrCodeImageAnalyzerListener {
                override fun onQRCodeFound(text: String) {
//                    qrCode = _qrCode
                    info { text }
//                    toast(text)
//                    qrCodeFoundButton.setVisibility(View.VISIBLE)
                }

                override fun qrCodeNotFound() {


                    info { "qrCodeNotFound" }
//                    qrCodeFoundButton.setVisibility(View.INVISIBLE)
                }
            })
        )

        val camera: Camera =
            cameraProvider.bindToLifecycle((this as LifecycleOwner), cameraSelector, imageAnalysis , preview)
    }

    /* ********************************************************************************************
     *                                          Permission
     */

    private fun requestPermission() {

        val snackbarPermission =
            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder.with(
                vBinding.motionLayout,
                "All those permissions are needed for this section"
            )
                .withOpenSettingsButton("Settings")
                .withDuration(4000)
                .build()

        val appPermissionsListener = object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {


                if (p0 != null) {
                    if (p0.areAllPermissionsGranted()) {
                        info { "areAllPermissionsGranted" }
                        Snackbar.make(vBinding.motionLayout, "Permission Granted !!", 4000).show()
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
                Snackbar.make(vBinding.motionLayout, it.toString(), 4000).show()
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