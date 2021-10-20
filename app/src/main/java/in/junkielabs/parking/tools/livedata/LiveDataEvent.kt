package `in`.junkielabs.parking.tools.livedata

/**
 * Created by Niraj on 03-05-2021.
 */
class LiveDataEvent<out T>(private val data: T) {

    var isConsumed = false
    private set


    fun data(): T = data

    fun dataIfNotConsumed(): T? {
        return if (isConsumed) {
            null
        } else {
            isConsumed = true
            data
        }
    }
}