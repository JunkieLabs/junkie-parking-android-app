package `in`.junkielabs.parking.ui.common.checkinout.dialogs

import `in`.junkielabs.parking.databinding.CheckinDialogBinding
import `in`.junkielabs.parking.databinding.CheckoutDialogBinding
import `in`.junkielabs.parking.tools.qrcode.QrCodeEncoder
import `in`.junkielabs.parking.ui.base.DialogBase
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.material.internal.ViewUtils
import com.google.zxing.BarcodeFormat

/**
 * Created by Niraj on 23-11-2021.
 */
class CheckOutDialog : DialogBase() {


    companion object {
        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): CheckOutDialog {
            var bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = CheckOutDialog()
            fragment.arguments = bf
            return fragment
        }

    }

    private var vBinding: CheckoutDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(0));
        vBinding = CheckoutDialogBinding.inflate(inflater, container, false)
        return  vBinding!!.root
//        return inflateView(inflater, container, R.layout.checkin_dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showQrCode("Dsdsd");

        vBinding?.checkoutDialogAnchor2?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                getView()?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                vBinding?.checkoutDialogTicketview?.addAnchor(vBinding!!.checkoutDialogAnchor2)

            }

        })

        //BarcodeEncoder

    }



    override fun onDestroy() {
        super.onDestroy()
        vBinding = null


    }

    private fun showQrCode(barcode: String){
        try {
            val barcodeEncoder = QrCodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(barcode, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = vBinding?.checkinDialogQrIv
            imageViewQrCode?.setImageBitmap(bitmap)
        } catch (e: Exception) {

        }

    }


}