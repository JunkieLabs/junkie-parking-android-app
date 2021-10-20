package `in`.junkielabs.parking.tools.livedata

import androidx.lifecycle.Observer

/**
 * Created by Niraj on 03-05-2021.
 */
class LiveDataObserver<T>(private val onEvent: (T) -> Unit) : Observer<LiveDataEvent<T>> {
    override fun onChanged(event: LiveDataEvent<T>?) {
        event?.dataIfNotConsumed()?.let {
            onEvent(it)
        }
    }
}