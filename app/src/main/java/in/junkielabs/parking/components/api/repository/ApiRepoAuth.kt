package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.models.auth.ParamAuthVerifyReq
import `in`.junkielabs.parking.components.api.routepoints.ApiPointAuth

/**
 * Created by Niraj on 20-10-2021.
 */
class ApiRepoAuth : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {
    private var apiPointAuth: ApiPointAuth = ApiModule.provideApiAuth()

    suspend fun verify(token: String) =
        requestForCookie {
            apiPointAuth.verify(ParamAuthVerifyReq(token))
        }

}