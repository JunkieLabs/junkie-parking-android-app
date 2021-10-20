package `in`.junkielabs.parking.components.api.models.guard

import java.io.Serializable

/**
 * Created by niraj on 07-02-2020.
 */
data class ParamGuard(var id: String,
                      var uid: String,
                      /** name */
                      var n: String?,
                      /** email */
                      var e: String?,
                      /** parking account id */
                      var pAcId: String?,
                      /** parking area id */
                      var pArId: String?,
                      /** avatar url */
                      var au: String?,
                      /** status */
                      var s: Number): Serializable {
}