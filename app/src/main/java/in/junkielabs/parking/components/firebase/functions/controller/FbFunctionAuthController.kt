package `in`.junkielabs.parking.components.firebase.functions.controller

import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

/**
 * Created by Niraj on 19-10-2021.
 */
class FbFunctionAuthController {


    val functions = Firebase.functions

    suspend fun verify(){
//        functions.getHttpsCallable("ss").
    }
}