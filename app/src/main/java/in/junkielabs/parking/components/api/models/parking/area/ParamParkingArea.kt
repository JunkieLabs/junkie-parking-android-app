package `in`.junkielabs.parking.components.api.models.parking.area

import `in`.junkielabs.parking.components.api.models.wheeler.ParamWheelerRate
import java.io.Serializable

/**
 * Created by niraj on 10-04-2020.
 */
data class ParamParkingArea(
    var id: String,

    /** name */
    var n: String?,
    /** parkingAccountId */
    var pAcId: String?, //id

    var rates: List<ParamWheelerRate>,) : Serializable {
}