package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.models.checkinout.ParamReqCheckInOut
import `in`.junkielabs.parking.components.api.routepoints.ApiPointCheckInOut

/**
 * Created by Niraj on 24-11-2021.
 */
class ApiRepoCheckInOut : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {
    private var apiPointCheckInOut: ApiPointCheckInOut = ApiModule.provideApiCheckInOut()

    suspend fun getById(accessToken: String, reqCheckInOut: ParamReqCheckInOut) =
        request {
            apiPointCheckInOut.checkInOut(accessToken, reqCheckInOut)
        }

}