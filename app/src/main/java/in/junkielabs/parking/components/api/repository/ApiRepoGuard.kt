package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.routepoints.ApiPointGuard

/**
 * Created by Niraj on 20-10-2021.
 */
class ApiRepoGuard : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {
    private var apiPointGuard: ApiPointGuard = ApiModule.provideApiGuard()

    suspend fun getById(accessToken: String, id: String) =
        request {
            apiPointGuard.byId(accessToken, id)
        }

}