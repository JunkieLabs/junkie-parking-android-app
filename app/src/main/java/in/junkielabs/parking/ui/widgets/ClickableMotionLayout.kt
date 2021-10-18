package `in`.junkielabs.parking.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.constraintlayout.motion.widget.MotionLayout

/**
 * Created by Niraj on 18-10-2021.
 */
class ClickableMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : MotionLayout(context, attrs) {
    private var mStartTime: Long = 0

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
//        return super.onInterceptHoverEvent(event)
        /*if (event?.action == MotionEvent.ACTION_MOVE) {
            return super.onInterceptTouchEvent(event)
        }
        return false*/
        Log.i("CLickerd", "${event?.action} ")

      /*  if ( event?.action == MotionEvent.ACTION_DOWN ) {
            mStartTime = event.eventTime;
        } else if ( event?.action == MotionEvent.ACTION_UP ) {
            if ( event?.eventTime - mStartTime <= ViewConfiguration.getTapTimeout() ) {
                Log.i("CLickerd", "eventup")
                return false;
            }else{
                Log.i("CLickerd", "eventup 2")

            }
        }

        return super.onInterceptTouchEvent(event);*/
        if ( event?.action == MotionEvent.ACTION_DOWN ) {
            mStartTime = event.eventTime;
        }
        if (event!=null &&  (event.eventTime.minus(mStartTime) >= ViewConfiguration.getTapTimeout()) && event.action == MotionEvent.ACTION_MOVE) {
            return super.onInterceptTouchEvent(event)
        }

        return false;
    }
}