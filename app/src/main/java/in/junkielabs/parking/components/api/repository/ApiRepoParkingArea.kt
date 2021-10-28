package `in`.junkielabs.parking.components.api.repository

import `in`.junkielabs.parking.components.api.ApiModule
import `in`.junkielabs.parking.components.api.base.ApiRepositoryBase
import `in`.junkielabs.parking.components.api.models.ParamErrorRes
import `in`.junkielabs.parking.components.api.routepoints.ApiPointParkingArea

/**
 * Created by Niraj on 28-10-2021.
 */
class ApiRepoParkingArea : ApiRepositoryBase<ParamErrorRes>(ParamErrorRes::class.java) {
    private var apiPointParkingArea: ApiPointParkingArea = ApiModule.provideApiParkingArea()

    suspend fun getById(accessToken: String, id: String) =
        request {
            apiPointParkingArea.byId(accessToken, id)
        }

}