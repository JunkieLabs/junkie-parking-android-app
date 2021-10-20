package `in`.junkielabs.parking.components.api.models.auth

import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Created by Niraj on 02-05-2021.
 */

@JsonClass(generateAdapter = true)
data class ParamAuthSignInRes(var contact: String?,
                              var username: String?,
                              var email: String?,
                              var passwordResetFlag: String,
                              var status: String ) : Serializable {

    // set Cookie

    /*
     *
     * username: nirajTest1
     * password: nirajTest
     */

}