package `in`.junkielabs.parking.ui.common.progress

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.FrameProgressDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * Created by Niraj on 22-10-2021.
 */
class FrameProgressDialog : DialogFragment() {


    companion object {
        @JvmStatic
        fun newInstance(b: Bundle?): FrameProgressDialog {
            val bf: Bundle = b ?: Bundle()
//            bf.putInt("fragment.key", key)
            val fragment = FrameProgressDialog()
            fragment.arguments = bf
            return fragment
        }

        const val TAG: String = "FrameProgressDialog"
    }

    lateinit var vBinding: FrameProgressDialogBinding


    /* override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setStyle(
             DialogFragment.STYLE_NORMAL,
             R.style.Tb_ThemeOverlay_MaterialComponents_BottomSheetDialog
         )
     }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.dr_transparent);

        vBinding = FrameProgressDialogBinding.inflate(inflater, container, false)

        return vBinding.root
    }

}