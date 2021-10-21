package `in`.junkielabs.parking.components.api.models.guard

import java.io.Serializable

/**
 * Created by niraj on 07-02-2020.
 */
data class ParamGuard(var id: String,
                      var uid: String,
                      /** name */
                      var name: String?,
                      /** email */
                      var email: String?,
                      /** parking account id */
                      var parkingAccountId: String?,
                      /** parking area id */
                      var parkingAreaId: String?,
                      /** avatar url */
                      var avatarUrl: String?,
                      /** status */
                      var status: Number): Serializable {
}