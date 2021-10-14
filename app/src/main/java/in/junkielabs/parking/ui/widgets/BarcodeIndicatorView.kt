package `in`.junkielabs.parking.ui.widgets

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.Nullable
import androidx.annotation.Px
import android.R.attr.endY

import com.google.android.material.internal.ViewUtils.dpToPx

import android.R.attr.left

import android.R.attr.top

import android.graphics.RectF

import android.graphics.PorterDuff

import android.graphics.PorterDuffXfermode





/**
 * Created by Niraj on 01-06-2021.
 */
class BarcodeIndicatorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mAnimLength: Float = 0.0F
    private var mAnimDirectionDown: Boolean = true
    private val DEFAULT_MASK_COLOR = 0x77000000
    private var mMaskPaint: Paint? = null
    private var mFramePaint: Paint? = null
    private var mLinePaint: Paint
    private var mPath: Path
    private var mFrameRect: Rect? = null
    private var mFrameCornersSize = 0
    private var mFrameCornersRadius = 0
    private var mFrameRatioWidth = 1f
    private var mFrameRatioHeight = 1f
    private var mFrameSize = 0.75f


    init {
        mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mMaskPaint?.style = Paint.Style.FILL
        mMaskPaint?.color = DEFAULT_MASK_COLOR
        mFramePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFramePaint?.style = Paint.Style.STROKE
        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = 4F;
        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        mPath = path
    }

    override fun onDraw(canvas: Canvas) {
        val frame = mFrameRect ?: return
        val widthF = width
        val heightF = height
        val top: Float = frame.top.toFloat()
        val left: Float = frame.left.toFloat()
        val right: Float = frame.right.toFloat()
        val bottom: Float = frame.bottom.toFloat()
        val frameCornersSize = mFrameCornersSize.toFloat()
        val frameCornersRadius = mFrameCornersRadius.toFloat()
        val path: Path = mPath
        if (frameCornersRadius > 0) {
            val normalizedRadius = Math.min(frameCornersRadius, Math.max(frameCornersSize - 1, 0f))
            path.reset()
            path.moveTo(left, top + normalizedRadius)
            path.quadTo(left, top, left + normalizedRadius, top)
            path.lineTo(right - normalizedRadius, top)
            path.quadTo(right, top, right, top + normalizedRadius)
            path.lineTo(right, bottom - normalizedRadius)
            path.quadTo(right, bottom, right - normalizedRadius, bottom)
            path.lineTo(left + normalizedRadius, bottom)
            path.quadTo(left, bottom, left, bottom - normalizedRadius)
            path.lineTo(left, top + normalizedRadius)
            path.moveTo(0.0f, 0.0f)
            path.lineTo(widthF.toFloat(), 0f)
            path.lineTo(widthF.toFloat(), heightF.toFloat())
            path.lineTo(0f, heightF.toFloat())
            path.lineTo(0f, 0f)
            canvas.drawPath(path, mMaskPaint!!)
            path.reset()
            path.moveTo(left, top + frameCornersSize)
            path.lineTo(left, top + normalizedRadius)
            path.quadTo(left, top, left + normalizedRadius, top)
            path.lineTo(left + frameCornersSize, top)
            path.moveTo(right - frameCornersSize, top)
            path.lineTo(right - normalizedRadius, top)
            path.quadTo(right, top, right, top + normalizedRadius)
            path.lineTo(right, top + frameCornersSize)
            path.moveTo(right, bottom - frameCornersSize)
            path.lineTo(right, bottom - normalizedRadius)
            path.quadTo(right, bottom, right - normalizedRadius, bottom)
            path.lineTo(right - frameCornersSize, bottom)
            path.moveTo(left + frameCornersSize, bottom)
            path.lineTo(left + normalizedRadius, bottom)
            path.quadTo(left, bottom, left, bottom - normalizedRadius)
            path.lineTo(left, bottom - frameCornersSize)
            canvas.drawPath(path, mFramePaint!!)
        } else {
            path.reset()
            path.moveTo(left, top)
            path.lineTo(right, top)
            path.lineTo(right, bottom)
            path.lineTo(left, bottom)
            path.lineTo(left, top)
            path.moveTo(0f, 0f)
            path.lineTo(widthF.toFloat(), 0f)
            path.lineTo(widthF.toFloat(), heightF.toFloat())
            path.lineTo(0f, heightF.toFloat())
            path.lineTo(0f, 0f)
            canvas.drawPath(path, mMaskPaint!!)
            path.reset()
            path.moveTo(left, top + frameCornersSize)
            path.lineTo(left, top)
            path.lineTo(left + frameCornersSize, top)
            path.moveTo(right - frameCornersSize, top)
            path.lineTo(right, top)
            path.lineTo(right, top + frameCornersSize)
            path.moveTo(right, bottom - frameCornersSize)
            path.lineTo(right, bottom)
            path.lineTo(right - frameCornersSize, bottom)
            path.moveTo(left + frameCornersSize, bottom)
            path.lineTo(left, bottom)
            path.lineTo(left, bottom - frameCornersSize)
            canvas.drawPath(path, mFramePaint!!)
        }
        drawLine(canvas, left, top, right, bottom)
    }

    override fun onLayout(
        changed: Boolean, left: Int, top: Int, right: Int,
        bottom: Int
    ) {
        invalidateFrameRect(right - left, bottom - top)
    }

    @Nullable
    fun getFrameRect(): Rect? {
        return mFrameRect
    }

    fun setFrameAspectRatio(
        @FloatRange(from = 0.0, fromInclusive = false) ratioWidth: Float,
        @FloatRange(from = 0.0, fromInclusive = false) ratioHeight: Float
    ) {
        mFrameRatioWidth = ratioWidth
        mFrameRatioHeight = ratioHeight
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.0, fromInclusive = false)
    fun getFrameAspectRatioWidth(): Float {
        return mFrameRatioWidth
    }

    fun setFrameAspectRatioWidth(
        @FloatRange(from = 0.0, fromInclusive = false) ratioWidth: Float
    ) {
        mFrameRatioWidth = ratioWidth
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.0, fromInclusive = false)
    fun getFrameAspectRatioHeight(): Float {
        return mFrameRatioHeight
    }

    fun setFrameAspectRatioHeight(
        @FloatRange(from = 0.0, fromInclusive = false) ratioHeight: Float
    ) {
        mFrameRatioHeight = ratioHeight
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    @ColorInt
    fun getMaskColor(): Int {
        return mMaskPaint!!.color
    }

    fun setMaskColor(@ColorInt color: Int) {
        mMaskPaint!!.color = color
        if (isLaidOut) {
            invalidate()
        }
    }

    @ColorInt
    fun getFrameColor(): Int {
        return mFramePaint!!.color
    }

    fun setFrameColor(@ColorInt color: Int) {
        mFramePaint!!.color = color
        mLinePaint!!.color = color
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameThickness(): Int {
        return mFramePaint!!.strokeWidth.toInt()
    }

    fun setFrameThickness(@Px thickness: Int) {
        mFramePaint!!.strokeWidth = thickness.toFloat()
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameCornersSize(): Int {
        return mFrameCornersSize
    }

    fun setFrameCornersSize(@Px size: Int) {
        mFrameCornersSize = size
        if (isLaidOut) {
            invalidate()
        }
    }

    @Px
    fun getFrameCornersRadius(): Int {
        return mFrameCornersRadius
    }

    fun setFrameCornersRadius(@Px radius: Int) {
        mFrameCornersRadius = radius
        if (isLaidOut) {
            invalidate()
        }
    }

    @FloatRange(from = 0.1, to = 1.0)
    fun getFrameSize(): Float {
        return mFrameSize
    }

    fun setFrameSize(@FloatRange(from = 0.1, to = 1.0) size: Float) {
        mFrameSize = size
        invalidateFrameRect()
        if (isLaidOut) {
            invalidate()
        }
    }

    private fun invalidateFrameRect() {
        invalidateFrameRect(width, height)
    }

    private fun drawLine(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float){

        val path: Path = mPath


        var offset = 4;
        if(mAnimDirectionDown && (mAnimLength + offset + top) > bottom ){
            mAnimDirectionDown = false
        }else if((mAnimLength - offset) < 0 ){
            mAnimDirectionDown = true
        }

        if(mAnimDirectionDown){
            mAnimLength += offset;
        }else{
            mAnimLength -= offset;
        }



        path.moveTo(left, top + mAnimLength)
        path.lineTo(right, top + mAnimLength)

        canvas.drawPath(path, mLinePaint);
        invalidate()


//        val cornerRadius = 0
//        val eraser = Paint()
//        eraser.isAntiAlias = true
//        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)


        /*val rect = RectF(
           left,
            top, right, bottom
        )
        canvas.drawRoundRect(rect, cornerRadius.toFloat(), cornerRadius.toFloat(), eraser)

        // draw horizontal line

        // draw horizontal line
        val line = Paint()
        line.color = lineColor
        line.strokeWidth = java.lang.Float.valueOf(lineWidth)

        // draw the line to product animation

        // draw the line to product animation
        if (endY >= attr.top + dpToPx(rectHeight) + frames) {
            revAnimation = true
        } else if (endY == attr.top + frames) {
            revAnimation = false
        }

        // check if the line has reached to bottom

        // check if the line has reached to bottom
        if (revAnimation) {
            endY -= frames
        } else {
            endY += frames
        }
        canvas.drawLine(attr.left, endY, attr.left + dpToPx(rectWidth), endY, line)*/
    }

    private fun invalidateFrameRect(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            val viewAR = width.toFloat() / height.toFloat()
            val frameAR = mFrameRatioWidth / mFrameRatioHeight
            val frameWidth: Int
            val frameHeight: Int
            if (viewAR <= frameAR) {
                frameWidth = Math.round(width * mFrameSize)
                frameHeight = Math.round(frameWidth / frameAR)
            } else {
                frameHeight = Math.round(height * mFrameSize)
                frameWidth = Math.round(frameHeight * frameAR)
            }
            val frameLeft = (width - frameWidth) / 2
            val frameTop = (height - frameHeight) / 2
            mFrameRect = Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight)
        }
    }


}