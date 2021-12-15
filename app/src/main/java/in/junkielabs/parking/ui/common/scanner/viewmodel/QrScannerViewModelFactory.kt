package `in`.junkielabs.parking.ui.common.scanner.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.repository.ApiRepoQrCode
import `in`.junkielabs.parking.components.api.repository.ApiRepoReport
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 15-12-2021.
 */
class QrScannerViewModelFactory (var application: ApplicationMy) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            ApiRepoQrCode::class.java
        ).newInstance(application, ApiRepoQrCode())
    }
}