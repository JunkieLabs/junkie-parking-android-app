package `in`.junkielabs.parking.components.api.base

/**
 * Created by Niraj on 02-05-2021.
 */
data class ApiResponse<out T, out E>(val status: Status, val data: T?, val errorData: E?, val message: String?, val setCookie: String? = null) {

    enum class Status {
        SUCCESS,
        ERROR,
        /** not in use */
        LOADING
    }

    companion object {
        fun <T, E> success(data: T): ApiResponse<T, E> {
            return ApiResponse(Status.SUCCESS, data, null, null)
        }



        fun <T, E> error(message: String, data: T? = null, errorData: E?): ApiResponse<T, E> {
            return ApiResponse(Status.ERROR, data, errorData,  message);
        }

        fun <T, E> loading(data: T? = null): ApiResponse<T, E> {
            return ApiResponse(Status.LOADING, data, null, null);
        }

        fun <T, E> successWithCookie(data: T, cookieParam: String): ApiResponse<T, E> {
            return ApiResponse(Status.SUCCESS, data, null, null, cookieParam)
        }
    }
}