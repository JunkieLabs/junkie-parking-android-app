package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.routepoints.ApiPointReport

/**
 * Created by Niraj on 08-12-2021.
 */
class ApiRepoReport : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {

    private var apiPointReport: ApiPointReport = ApiModule.provideApiReport()

    suspend fun forParkingArea(accessToken: String, parkingAreaId: String) =
        request {
            apiPointReport.reports(accessToken, parkingAreaId)
        }
}