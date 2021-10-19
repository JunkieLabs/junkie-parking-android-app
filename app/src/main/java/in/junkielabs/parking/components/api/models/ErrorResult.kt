package `in`.junkielabs.parking.components.api.models

import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 02-05-2021.
 */
// https://itnext.io/android-architecture-hilt-mvvm-kotlin-coroutines-live-data-room-and-retrofit-ft-8b746cab4a06

interface ErrorResult {
    fun code(): Int?
    fun message(): String
}