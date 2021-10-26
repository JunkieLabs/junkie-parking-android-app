package `in`.junkielabs.parking.components.api.base

import `in`.junkielabs.parking.components.api.models.ErrorResult
import android.util.Log
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Niraj on 02-05-2021.
 */
abstract class ApiRepositoryBase<out E : ErrorResult>(private val typeE: Class<E>)  {

    @Inject
    lateinit var mMosi: Moshi


    @Deprecated("old implementation, will be removed in future")
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ApiResponse<T, E> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ApiResponse.success(body)
            }
            var message = response.message()
            var errorRes: E? = null
            if (response.errorBody() != null) {
                val adapter = mMosi.adapter<E>(typeE)

                errorRes = adapter.fromJson(response.errorBody()!!.source())
                if (errorRes != null) {
                    message = errorRes.message()
                }

            }
//            info { "getResult 2, ${message}" }
            //" ${response.code()} ${message}"
            return error(message, errorRes)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {


            }
            return error(e.message ?: e.toString(), null)
        }
    }

    @Deprecated("old implementation, will be removed in future")
    protected suspend fun <T> getResultWithCookie(call: suspend () -> Response<T>): ApiResponse<T, E> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                val setCookie = response.headers()["Set-Cookie"];
                if (body != null && setCookie != null) return ApiResponse.successWithCookie(
                    body,
                    cookieParam = setCookie
                )
            }
            var message = response.message()
            var errorRes: E? = null
            if (response.errorBody() != null) {
                val adapter = mMosi.adapter<E>(typeE)

                errorRes = adapter.fromJson(response.errorBody()!!.source())
                if (errorRes != null) {
                    message = errorRes.message()
                }

            }
//            info { "getResult 2, ${message}" }
            //" ${response.code()} ${message}"
            return error(message, errorRes)
        } catch (e: Exception) {
            if (e is HttpException) {


            }
            return error(e.message ?: e.toString(), null)
        }
    }



    protected suspend fun <T> request(call: suspend () -> Response<T>): ApiResponse<T, E> {
        try {
            val response = call()
            if (response.isSuccessful) {
//                info{"responce successful"}
                val body = response.body()
                if (body != null) return ApiResponse.success(body)
            }
            var message = response.message()
            var errorRes: E? = null
            if (response.errorBody() != null) {
                val adapter = mMosi.adapter<E>(typeE)

                errorRes = adapter.fromJson(response.errorBody()!!.source())
                if (errorRes != null) {
                    message = errorRes.message()
                }

            }
//            info { "getResult 2, ${message}" }
            //" ${response.code()} ${message}"
            return error(message, errorRes)
        } catch (e: Exception) {

//            info { "Error in request -------- " }
            e.printStackTrace()
            if (e is HttpException) {


            }
            return error(e.message ?: e.toString(), null)
        }
    }

    protected suspend fun <T> requestForCookie(call: suspend () -> Response<T>): ApiResponse<T, E> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                val setCookie = response.headers()["Set-Cookie"];

//                info{"requestForCookie: ${response.headers()}"}
                if (body != null && setCookie != null) return ApiResponse.successWithCookie(
                    body,
                    cookieParam = setCookie
                )
            }
            var message = response.message()
            var errorRes: E? = null
            if (response.errorBody() != null) {
                val adapter = mMosi.adapter<E>(typeE)

                errorRes = adapter.fromJson(response.errorBody()!!.source())
                if (errorRes != null) {
                    message = errorRes.message()
                }

            }
//            info { "getResult 2, ${message}" }
            //" ${response.code()} ${message}"
            return error(message, errorRes)
        } catch (e: Exception) {
            if (e is HttpException) {


            }
            return error(e.message ?: e.toString(), null)
        }
    }

    //    private fun <T> errorParse(exception: HttpException): ApiResponse<T> {
//        error { "remoteDataSource $message " }
//        return ApiResponse.error("Network call has failed for a following reason: $message")
//    }
    private fun <T> error(message: String, errorBody: E?): ApiResponse<T, E> {
        Log.e( "ApiRepositoryBase","remoteDataSource $message $errorBody " )
//        return ApiResponse.error("Network call has failed for a following reason: $message")
        return ApiResponse.error(message, null, errorBody)
    }
}