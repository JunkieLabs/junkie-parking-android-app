package `in`.junkielabs.parking.utils

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils




/**
 * Created by Niraj on 22-11-2021.
 */
object UtilAnimation {
    fun revealView(view: View, startX: Int, startY: Int, h: Int, w: Int , listener: Animator.AnimatorListener?){
      /*  val w = view.width
        val h = view.height
*/
        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toInt()

        val cx = startX//(fab.getX() + fab.getWidth() / 2)
        val cy: Int = startY//fab.getY() as Int + fab.getHeight() + 56
        val revealAnimator: Animator =
            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, endRadius.toFloat())

        view.visibility = View.VISIBLE
        revealAnimator.duration = 400

        revealAnimator.addListener(listener)
        revealAnimator.start()

    }
}