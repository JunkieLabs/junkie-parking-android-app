package `in`.junkielabs.parking.ui.base

import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

/**
 * Created by Niraj on 22-11-2021.
 */
open class DialogBase : DialogFragment() {


    fun configureDialogView(){
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
//        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        dialog?.window?.addFlags(0x00000004)
//        dialog?.window?.z
    }


    fun inflateView(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layoutResID: Int): View {
        configureDialogView()

        return inflater.inflate(layoutResID, container, false)
    }
}