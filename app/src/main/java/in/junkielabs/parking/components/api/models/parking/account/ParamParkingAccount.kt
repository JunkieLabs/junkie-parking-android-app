package `in`.junkielabs.parking.components.api.models.parking.account

import java.io.Serializable

/**
 * Created by niraj on 10-04-2020.
 */
data class ParamParkingAccount(var id: String,
                               /** name */
                               var n: String) : Serializable {
}