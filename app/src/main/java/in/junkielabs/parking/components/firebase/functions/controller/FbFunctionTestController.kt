package `in`.junkielabs.parking.components.firebase.functions.controller

import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by Niraj on 19-10-2021.
 */
class FbFunctionTestController: AnkoLogger {

    val functions = Firebase.functions

    suspend fun test(text: String): Any? {
        var map = mapOf(Pair("text", text))
        try {
            var result = functions.getHttpsCallable("parkingApi/tests").call(map).await()
            if (result != null) {
                return result.data
            }
        } catch (e: Exception) {
            if (e is FirebaseFunctionsException) {
                val code = e.code
                val details = e.details
                info { "code: $code, $details" }
            }

            e.printStackTrace()
        }
        return null


    }
}