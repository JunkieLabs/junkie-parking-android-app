package `in`.junkielabs.parking.components.api.internal

/**
 * Created by Niraj on 16-05-2021.
 */
class MethodRequestCache {

    private val map = HashMap<Int, String>()

    fun register(requestIdentifier: Int, tokenType: String) {
        map[requestIdentifier] = tokenType
    }

    fun getCredentialType(requestIdentifier: Int) = map[requestIdentifier]
}