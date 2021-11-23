package `in`.junkielabs.parking.ui.widgets

import `in`.junkielabs.parking.R
import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import android.graphics.PorterDuff.Mode
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.RelativeLayout
import android.graphics.PorterDuffXfermode
import android.util.Log

/**
 * Created by Niraj on 22-11-2021.
 */
class TicketView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    companion object {
        private val DEFAULT_RADIUS: Float = 9f
        private val NO_VALUE = -1
    }

    private val eraser = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mLayoutAnchorViewId: Int? = null

    private var circlesPath = Path()
    private var mCirclePosition: Float = 0f
    private var circleRadius: Float = 0f
    private var circleSpace: Float = 0f

    private var dashColor: Int = 0
    private var dashSize: Float = 0f
    private val dashPath = Path()
    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mCirclePositionsMap = mutableMapOf<Int, Float>()

    init {

        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        val a = context.obtainStyledAttributes(attrs, R.styleable.TicketView)
        try {
            circleRadius = a.getDimension(
                R.styleable.TicketView_tv_circleRadius,
                getDp(DEFAULT_RADIUS).toFloat()
            )
            mLayoutAnchorViewId = a.getResourceId(R.styleable.TicketView_tv_anchor, NO_VALUE)
            circleSpace =
                a.getDimension(R.styleable.TicketView_tv_circleSpace, getDp(15f).toFloat())
            dashColor = a.getColor(R.styleable.TicketView_tv_dashColor, Color.parseColor("#0085be"))
            dashSize = a.getDimension(R.styleable.TicketView_tv_dashSize, getDp(1.5f).toFloat())
        } finally {
            a.recycle()
        }

        eraser.xfermode = PorterDuffXfermode(Mode.CLEAR)

        dashPaint.color = dashColor
        dashPaint.style = Style.STROKE
        dashPaint.strokeWidth = dashSize
        dashPaint.pathEffect =
            DashPathEffect(floatArrayOf(getDp(3f).toFloat(), getDp(3f).toFloat()), 0f)
    }

    fun setRadius(radius: Float) {
        this.circleRadius = radius
        postInvalidate()
    }

    fun setAnchor(view: View?) {

        val rect = Rect()
        view?.getDrawingRect(rect)
        offsetDescendantRectToMyCoords(view, rect)
        mCirclePosition = rect.bottom.toFloat()


        postInvalidate()
    }

    fun addAnchor(view: View) {
        val rect = Rect()
        view.getDrawingRect(rect)
        offsetDescendantRectToMyCoords(view, rect)
        mCirclePositionsMap[view.id] = rect.bottom.toFloat()

        Log.i("TicketView", "addAnchor ${rect.bottom.toFloat()}")
        postInvalidate()
    }

    fun removeAnchor(view: View) {
        if (mCirclePositionsMap.containsKey(view.id)) {
            mCirclePositionsMap.remove(view.id)
            postInvalidate()
        }

    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        val drawChild = super.drawChild(canvas, child, drawingTime)
        drawHoles(canvas!!)
        return drawChild
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.save()
        super.dispatchDraw(canvas)
        canvas?.restore()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mLayoutAnchorViewId != null) {
            val anchorView = findViewById<View>(mLayoutAnchorViewId!!)
            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    setAnchor(anchorView)
                }
            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawHoles(canvas)
        super.onDraw(canvas)
    }

    private fun drawHoles(canvas: Canvas) {
        circlesPath = Path()
        val w = width
        val radius = circleRadius
        val space = circleSpace
        val circleWidth = radius * 2

        var leftMargin = 0
        if (layoutParams is MarginLayoutParams) {
            val lp = layoutParams as MarginLayoutParams
            leftMargin = lp.leftMargin
        }

        val left = left - leftMargin
        val circleSpace = circleWidth + space
        val count = (w / circleSpace).toInt()
        val offset = w - circleSpace * count
        val sideOffset = offset / 2
        val halfCircleSpace = circleSpace / 2

        /*for (i in 0 until count) {
            var positionCircle = i * circleSpace + sideOffset + left.toFloat() - radius
            if (i == 0) {
                positionCircle = left + sideOffset - radius
            }
            this.circlesPath.addCircle(positionCircle + halfCircleSpace, -circleRadius / 4, radius, Path.Direction.CW)
        }*/

        // add holes on the ticketView by erasing them
        with(circlesPath) {
            //anchor1
            addCircle(
                -circleRadius / 4,
                mCirclePosition,
                circleRadius,
                Path.Direction.CW
            ) // bottom left hole
            addCircle(
                w + circleRadius / 4,
                mCirclePosition,
                circleRadius,
                Path.Direction.CW
            )// bottom right hole

            mCirclePositionsMap.values.forEach {
                addCircle(
                    -circleRadius / 4,
                    it,
                    circleRadius,
                    Path.Direction.CW
                ) // bottom left hole
                addCircle(
                    w + circleRadius / 4,
                    it,
                    circleRadius,
                    Path.Direction.CW
                )// bottom right hole

            }


        }

        with(dashPath) {
            //anchor1
            moveTo(circleRadius, mCirclePosition)
            quadTo(w - circleRadius, mCirclePosition, w - circleRadius, mCirclePosition)

            mCirclePositionsMap.values.forEach {
                moveTo(circleRadius, it)
                quadTo(w - circleRadius, it, w - circleRadius, it)


            }

        }

        with(canvas) {
            if (dashSize > 0)
                drawPath(dashPath, dashPaint)
            drawPath(circlesPath, eraser)
        }
    }


    private fun getDp(value: Float): Int {
        return when (value) {
            0f -> 0
            else -> {
                val density = resources.displayMetrics.density
                Math.ceil((density * value).toDouble()).toInt()
            }
        }
    }
}