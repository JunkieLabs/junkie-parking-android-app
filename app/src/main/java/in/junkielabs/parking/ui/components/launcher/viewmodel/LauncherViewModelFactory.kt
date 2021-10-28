package `in`.junkielabs.parking.ui.components.launcher.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoGuard
import `in`.junkielabs.parking.components.api.repository.ApiRepoParkingArea
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 20-10-2021.
 */
class LauncherViewModelFactory(var application: ApplicationMy) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            ApiRepoAuth::class.java,
            ApiRepoGuard::class.java,
            ApiRepoParkingArea::class.java
        )
            .newInstance(application, ApiRepoAuth(), ApiRepoGuard(), ApiRepoParkingArea())
    }

}