package `in`.junkielabs.parking.ui.widgets


import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.ui.widgets.internal.PagerIndicator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.AnkoLogger
import android.util.TypedValue
import android.os.Build
import androidx.annotation.RequiresApi
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.Nullable

/**
 * Created by niraj on 24-04-2021.
 */


class PagerIndicatorView: View, AnkoLogger {


    private var mColor: Int = 0

    private val indicator = PagerIndicator()
    private var listener: PagerListener? = null

    private val pagerCallback = object : PagerCallback {
        override fun setItemCount(itemCount: Int) {
            this@PagerIndicatorView.setItemCount(itemCount)
        }

        override fun setPageScrolled(position: Int, positionOffset: Float) {
            currentPosition = position
            currentPositionOffset = positionOffset
            invalidate()
        }
    }

    @JvmOverloads constructor(context: Context) : this(context, null)

    @JvmOverloads constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int):  super(context, attrs, defStyleAttr){
        init(context, attrs, defStyleAttr, R.style.PagerIndicator_Line)
        if (isInEditMode) {
            itemCount = indicator.getMaxDisplayedItems()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context,
        attrs, defStyleAttr, defStyleRes) {
        if (isInEditMode) {
            itemCount = 3
        }
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PagerIndicatorView, defStyleAttr, defStyleRes)

        val dm = resources.displayMetrics
        val dp2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, dm)
        val size = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_size, dp2.toInt())
        indicator.setItemSize(size)

        val dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, dm)
        val padding = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_padding, dp4.toInt())
        indicator.setItemPadding(padding.toFloat())

        val dp12 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, dm)
        val length = a.getDimensionPixelSize(R.styleable.PagerIndicatorView_pi_length, dp12.toInt())
        indicator.setItemLength(length.toFloat())

        val edgeAnimationFlags = a.getInt(R.styleable.PagerIndicatorView_pi_edgeAnimation, PagerIndicator.FLAG_ALPHA)
        indicator.setEdgeAnimationFlags(edgeAnimationFlags)

        val type = a.getInt(R.styleable.PagerIndicatorView_pi_type, PagerIndicator.TYPE_LINE)
        indicator.setType(type)

        val colorBackground = a.getColor(R.styleable.PagerIndicatorView_android_colorBackground, Color.GRAY)
        indicator.setColorBackground(colorBackground)

        val colorFocused = a.getColor(R.styleable.PagerIndicatorView_android_colorFocusedHighlight, Color.WHITE)
        indicator.setColorFocused(colorFocused)

        val maxDisplayedItems = a.getInt(R.styleable.PagerIndicatorView_pi_maxVisibleItems, 10)
        indicator.setMaxDisplayedItems(maxDisplayedItems)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width: Int
        when (widthMode) {
            MeasureSpec.EXACTLY -> width = MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> width = Math.min(
                indicator.getWidth(itemCount) + paddingLeft + paddingRight,
                MeasureSpec.getSize(widthMeasureSpec)
            )
            else -> throw IllegalArgumentException()
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height: Int
        when (heightMode) {
            MeasureSpec.EXACTLY -> height = MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> height = Math.min(
                indicator.getHeight(itemCount) + paddingTop + paddingBottom,
                MeasureSpec.getSize(heightMeasureSpec)
            )
            else -> throw IllegalArgumentException()
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        indicator.setBounds(
            width - paddingLeft - paddingRight,
            height - paddingTop - paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        indicator.draw(canvas, itemCount, currentPosition, currentPositionOffset)
        canvas.restoreToCount(save)
    }

    private var itemCount = 0
    private var currentPosition = 0
    private var currentPositionOffset = 0f

    fun setPager(@Nullable pager: PagerListener) {
        if (listener != null) {
            listener!!.setPagerCallback(null)
        }
        this.listener = pager
        if (listener != null) {
            listener!!.setPagerCallback(pagerCallback)
        }
    }

    private fun setItemCount(itemCount: Int) {
        if (this.itemCount == itemCount) {
            return
        }
        this.itemCount = itemCount
        requestLayout()
    }

    interface PagerCallback {
        fun setItemCount(itemCount: Int)

        fun setPageScrolled(position: Int, positionOffset: Float)
    }

    interface PagerListener {
        fun setPagerCallback(@Nullable callback: PagerCallback?)
    }


}