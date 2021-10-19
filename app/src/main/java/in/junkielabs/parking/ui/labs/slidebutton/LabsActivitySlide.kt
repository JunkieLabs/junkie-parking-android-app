package `in`.junkielabs.parking.ui.labs.slidebutton

import kotlinx.coroutines.tasks.await
import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.ActivityLabsSlideBinding
import `in`.junkielabs.parking.ui.base.ActivityBase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.info

class LabsActivitySlide : ActivityBase() {
    private lateinit var vBinding: ActivityLabsSlideBinding
    private lateinit var functions: FirebaseFunctions

    // ...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vBinding = ActivityLabsSlideBinding.inflate(layoutInflater)
        setStatusDefault(false)
        setContentView(vBinding.root)
        vBinding.activityLabsSlideButton.setOnClickListener {
            info { "clciked" }
            var ewew = vBinding.activityLabsSlideMotionLayout.currentState
            if (ewew == vBinding.activityLabsSlideMotionLayout.startState) {
                vBinding.activityLabsSlideMotionLayout.transitionToEnd()

            } else {
                vBinding.activityLabsSlideMotionLayout.transitionToStart()

            }
        }

        functions = Firebase.functions

//        setContentView(R.layout.activity_labs_slide_button)
    }

    /*suspend fun dsds() {
        functions
            .getHttpsCallable("addMessage").call().await()
    }*/
}