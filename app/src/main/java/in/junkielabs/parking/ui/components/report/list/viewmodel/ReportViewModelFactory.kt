package `in`.junkielabs.parking.ui.components.report.list.viewmodel

import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.repository.ApiRepoCheckInOut
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by Niraj on 10-12-2021.
 */
class ReportViewModelFactory(var application: ApplicationMy): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java,
            ApiRepoCheckInOut::class.java)
            .newInstance(application, ApiRepoCheckInOut())
    }
}