package `in`.junkielabs.parking.ui.common.checkinout.dialogs

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.ui.base.DialogBase
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Niraj on 22-11-2021.
 */
class CheckInDialog : DialogBase() {


    companion object {
        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): CheckInDialog {
            var bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = CheckInDialog()
            fragment.arguments = bf
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(0));
        return inflateView(inflater, container, R.layout.checkin_dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



    override fun onDestroy() {
        super.onDestroy()


    }


}