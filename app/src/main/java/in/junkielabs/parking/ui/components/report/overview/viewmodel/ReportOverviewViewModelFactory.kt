package `in`.junkielabs.parking.ui.components.report.overview.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.repository.ApiRepoReport
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 08-12-2021.
 */
class ReportOverviewViewModelFactory (var application: ApplicationMy) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            ApiRepoReport::class.java
        ).newInstance(application, ApiRepoReport())
    }
}