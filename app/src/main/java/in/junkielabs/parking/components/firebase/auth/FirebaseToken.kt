package `in`.junkielabs.parking.components.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Niraj on 19-10-2021.
 */
object FirebaseToken {
    suspend fun getToken() = suspendCoroutine<String?> {

//        info { "FirebaseToken 1" }
        val mUser = FirebaseAuth.getInstance().currentUser
//        info { "FirebaseToken 2" }
//            emit(null)
        mUser?.getIdToken(true)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    /*info { idToken }
                    info { "hre" }*/
                    it.resume("Bearer $idToken")
//                        offer(null)
                    // Send token to your backend via HTTPS
                    // ...
                } else {
//                    info { "FirebaseToken 3" }
                    it.resume(null)
                    // Handle error -> task.getException();
                }
            }
    }

    suspend fun getUserIdToken() = suspendCoroutine<String?> {

//        info { "FirebaseToken 1" }
        val mUser = FirebaseAuth.getInstance().currentUser
//        info { "FirebaseToken 2" }
//            emit(null)
        mUser?.getIdToken(false)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token
                    /*info { idToken }
                    info { "hre" }*/
                    it.resume(idToken)
//                        offer(null)
                    // Send token to your backend via HTTPS
                    // ...
                } else {
//                    info { "FirebaseToken 3" }
                    it.resume(null)
                    // Handle error -> task.getException();
                }
            }
    }

}