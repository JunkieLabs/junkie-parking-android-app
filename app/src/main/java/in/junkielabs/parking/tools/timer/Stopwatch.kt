package `in`.junkielabs.parking.tools.timer

import android.os.CountDownTimer

/**
 * Created by niraj on 03-01-2020.
 */
class Stopwatch(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        callback?.onTick(millisUntilFinished);

    }

    override fun onFinish() {
        try {
            callback?.timerExpired();
        }catch (e: Exception){
            e.printStackTrace()
//            Crashlytics.logException(e)
        }

    }


    private var callback : StopwatchCallback?= null;


    fun setCallback(callback: StopwatchCallback?) {
        this.callback = callback
    }

    interface StopwatchCallback {

        fun timerExpired();
        fun onTick(millisUntilFinished: Long);
    }
}