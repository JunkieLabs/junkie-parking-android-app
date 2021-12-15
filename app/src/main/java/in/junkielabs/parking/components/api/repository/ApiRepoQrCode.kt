package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.routepoints.ApiPointQrCode

/**
 * Created by Niraj on 15-12-2021.
 */
class ApiRepoQrCode : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {
    private var apiPointQrCode: ApiPointQrCode = ApiModule.provideApiQrCode()

    suspend fun getVehicle(token: String, qrCode: String) =
        request {
            apiPointQrCode.vehicle(token, qrCode)
        }
}