package `in`.junkielabs.parking.ui.components.home.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.repository.ApiRepoAuth
import `in`.junkielabs.parking.components.api.repository.ApiRepoCheckInOut
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 28-10-2021.
 */
class HomeViewModelFactory(var application: ApplicationMy) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            ApiRepoAuth::class.java,
            ApiRepoCheckInOut::class.java
        )
            .newInstance(application, ApiRepoAuth(), ApiRepoCheckInOut())
    }


}