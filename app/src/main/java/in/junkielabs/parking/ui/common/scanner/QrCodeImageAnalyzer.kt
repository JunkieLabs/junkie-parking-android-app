package `in`.junkielabs.parking.ui.common.scanner

import android.graphics.ImageFormat.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import java.nio.ByteBuffer


/**
 * Created by Niraj on 31-05-2021.
 */
class QrCodeImageAnalyzer(var mListener: QrCodeImageAnalyzerListener?) : ImageAnalysis.Analyzer {
    private var mIsAnalysing: Boolean  = true

    override fun analyze(image: ImageProxy) {

        if(mIsAnalysing) {

            if (image.format == YUV_420_888 || image.format == YUV_422_888 || image.format == YUV_444_888) {
                val byteBuffer: ByteBuffer = image.planes[0].buffer
                val imageData = ByteArray(byteBuffer.capacity())
                byteBuffer.get(imageData)
                val source = PlanarYUVLuminanceSource(
                    imageData,
                    image.width, image.height,
                    0, 0,
                    image.width, image.height,
                    false
                )
                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                try {
//                info { "analysing" }
                    val result = QRCodeMultiReader().decode(binaryBitmap)
                    mListener?.onQRCodeFound(result.text)
                } catch (e: FormatException) {
                    mListener?.qrCodeNotFound()
                } catch (e: ChecksumException) {
                    mListener?.qrCodeNotFound()
                } catch (e: NotFoundException) {
                    mListener?.qrCodeNotFound()
                }
            }
        }

        image.close()
    }

    fun shouldAnalyse(isAnalysing: Boolean){
        mIsAnalysing = isAnalysing;

    }

    interface QrCodeImageAnalyzerListener {
        fun onQRCodeFound(text: String);
        fun qrCodeNotFound()
    }
}