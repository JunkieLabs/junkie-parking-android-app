package `in`.junkielabs.parking.components.account


/**
 * Created by Niraj on 03-05-2021.
 */
object AccountConstants {


    object Account {
        const val ACTION_SIGNIN = "signin"
        const val ACTION_REAUTH = "reauth"
        const val ACTION_REMOVE = "remove"

        object Arguments {
            const val ACCOUNT_ACTION = "account.action"

        }
    }

    object AccountResult {
        const val CODE_OK = 200
        const val CODE_FAILED = 400

        object Arguments {

            const val CODE = "accountResult.code"

            //            val FAILURE_MESSAGE = "account.failureMessage"
            const val MESSAGE = "accountResult.message"
        }
    }

    object AccountUser {
        const val STATE_NOT_EXIST = 100
        const val STATE_AUTHORIZED = 101
        const val STATE_REAUTH = 102
        const val STATE_WAITING = 103



    }









}