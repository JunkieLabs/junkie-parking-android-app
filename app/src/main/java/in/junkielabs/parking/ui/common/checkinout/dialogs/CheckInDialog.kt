package `in`.junkielabs.parking.ui.common.checkinout.dialogs

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.components.api.models.checkinout.ParamCheckInOut
import `in`.junkielabs.parking.components.parking.ParkingConstants
import `in`.junkielabs.parking.databinding.CheckinDialogBinding
import `in`.junkielabs.parking.tools.qrcode.QrCodeEncoder
import `in`.junkielabs.parking.ui.base.DialogBase
import `in`.junkielabs.parking.utils.UtilDate
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat

/**
 * Created by Niraj on 22-11-2021.
 */
class CheckInDialog : DialogBase() {


    companion object {

        val B_ARG_CHECKINOUT: String =
            CheckOutDialog::class.java.simpleName + ".arg_checkinout"

        @JvmStatic
        fun newInstance(key: Int, b: Bundle?): CheckInDialog {
            var bf: Bundle = b ?: Bundle()
            bf.putInt("fragment.key", key);
            val fragment = CheckInDialog()
            fragment.arguments = bf
            return fragment
        }

    }

    private var mCheckInOut: ParamCheckInOut? = null
    private var vBinding: CheckinDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCheckInOut = arguments?.getParcelable<ParamCheckInOut>(CheckOutDialog.B_ARG_CHECKINOUT)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(0));
        vBinding = CheckinDialogBinding.inflate(inflater, container, false)
        return  vBinding!!.root
//        return inflateView(inflater, container, R.layout.checkin_dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //BarcodeEncoder
        if(mCheckInOut!=null){
            vBinding?.checkinDialogQrTv?.text = mCheckInOut!!.qrCode
            vBinding?.checkinDialogTextVehicleNumber?.text = mCheckInOut!!.vehicleNumber
            vBinding?.checkinDialogTextVehicleType?.text = ParkingConstants.Wheeler.getWheelerType(mCheckInOut!!.wheelerRate.type)
            vBinding?.checkinDialogTextStartTime?.text =  UtilDate.getFormattedDateTime(mCheckInOut!!.inTimestamp,
                UtilDate.FORMAT_DATE_READABLE2, requireContext())

            // mCheckInOut!!.inTimestamp
            showQrCode(mCheckInOut!!.qrCode)
        }

        vBinding?.checkinDialogBtnOk?.setOnClickListener {
            dismiss()
        }


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

    interface CheckInDialogListener{
        fun onCheckInDialogCancel()
        fun onCheckInDialogOk()
    }
}