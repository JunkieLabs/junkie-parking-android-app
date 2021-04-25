package `in`.junkielabs.parking.ui.widgets.internal

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.min

/**
 * Created by niraj on 24-04-2021.
 */
class PagerIndicator() {


    companion object{

        const val FLAG_SIZE = 1
        const val FLAG_ALPHA = 2

        const val TYPE_LINE = 0
        const val TYPE_CIRCLE = 1
    }

    private var edgeAnimationFlags: Int = 0
    private var type: Int = 0

    private var itemLength: Float = 0.toFloat()
    private var itemSize: Float = 0.toFloat()
    private var itemPadding: Float = 0.toFloat()

    private val bounds = Rect()
    private var maxDisplayedItems = 3

    private val paint = Paint()
    private var colorFocused: Int = 0
    private var colorBackground: Int = 0

    init {
        paint.setStrokeCap(Paint.Cap.ROUND)
        paint.setAntiAlias(true)
    }

    fun getWidth(items: Int): Int {
        val actualItems = Math.min(maxDisplayedItems, items)
        return if (actualItems == 0) 0 else (actualItems * itemLength + (actualItems - 1) * itemPadding).toInt()
    }

    fun getHeight(items: Int): Int {
        return itemSize.toInt()
    }

    fun setBounds(width: Int, height: Int) {
        bounds.set(0, 0, width, height)
    }

    fun getBounds(): Rect {
        return bounds
    }

    fun draw(canvas: Canvas, items: Int, active: Int, progress: Float) {
        val nonAnimatedOffset = maxDisplayedItems / 2

        val isAnimating =
            maxDisplayedItems < items && active >= nonAnimatedOffset && active < items - nonAnimatedOffset - 1
        val animationOffset: Float
        if (isAnimating) {
            animationOffset = (itemLength + itemPadding) * -progress
        } else {
            animationOffset = 0f
        }

        val bounds = getBounds()
        val maxPossibleItems = (1 + (bounds.width() - itemLength) / (itemLength + itemPadding)).toInt()
        val itemsToDraw = min(maxPossibleItems, min(maxDisplayedItems, items))

        val itemMeasuredWidth = getWidth(itemsToDraw)
        val itemMeasuredHeight = getHeight(itemsToDraw)
        val offsetX = (bounds.width() - itemMeasuredWidth) / 2
        val offsetY = bounds.height() - itemMeasuredHeight

        val saveBackground = canvas.save()
        canvas.translate(offsetX + animationOffset, offsetY.toFloat())
        drawBackground(canvas, progress, isAnimating, itemsToDraw)
        canvas.restoreToCount(saveBackground)

        val saveActive = canvas.save()
        canvas.translate(offsetX + animationOffset, offsetY.toFloat())
        val activeItem = getNormalizedActiveItem(items, active, nonAnimatedOffset)
        drawActiveIndicator(canvas, progress, activeItem)
        canvas.restoreToCount(saveActive)
    }

    private fun drawBackground(canvas: Canvas, progress: Float, isAnimating: Boolean, itemsToDraw: Int) {
        // animate the first item if scrolling
        drawIndicator(canvas, if (isAnimating) 1 - progress else 1f)
        canvas.translate(itemLength + itemPadding, 0f)

        for (i in 1 until itemsToDraw) {
            drawIndicator(canvas, 1f)
            canvas.translate(itemLength + itemPadding, 0f)
        }

        if (isAnimating && progress > 0f) {
            // animate the next item
            drawIndicator(canvas, progress)
        }
    }

    private fun getNormalizedActiveItem(items: Int, active: Int, nonAnimatedOffset: Int): Int {
        val normalizedActiveItem: Int
        if (active < nonAnimatedOffset) {
            normalizedActiveItem = active
        } else if (items - nonAnimatedOffset <= active) {
            normalizedActiveItem = maxDisplayedItems - (items - active)
        } else {
            normalizedActiveItem = nonAnimatedOffset
        }
        return normalizedActiveItem
    }

    private fun drawActiveIndicator(canvas: Canvas, progress: Float, activeItem: Int) {
        canvas.translate(activeItem * itemLength + activeItem * itemPadding, 0f)
        drawActiveIndicator(canvas, progress, 1f)
        if (progress != 0f) {
            canvas.translate(itemLength + itemPadding, 0f)
            drawActiveIndicator(canvas, -progress, progress)
        }
    }

    /**
     * Draw the indicator background.
     *
     * @param canvas     the canvas to draw to
     * @param visibility a value [0, 1] indicating an ongoing animation.
     * Use this to scale / fade indicators on the edges.
     */
    protected fun drawIndicator(canvas: Canvas, visibility: Float) {
        paint.setColor(colorBackground)
        paint.setStrokeWidth(itemSize)
        if (edgeAnimationFlags and FLAG_ALPHA == FLAG_ALPHA) {
            paint.setAlpha((visibility * Color.alpha(colorBackground)).toInt())
        }
        if (type == TYPE_LINE) {
            paint.setStyle(Paint.Style.STROKE)
            paint.setStrokeWidth(itemSize)
            canvas.drawLine(
                itemPadding / 2,
                itemSize / 2,
                itemLength - itemPadding / 2,
                itemSize / 2,
                paint
            )
        } else if (type == TYPE_CIRCLE) {
            paint.setStyle(Paint.Style.FILL)
            val radius: Float
            if (edgeAnimationFlags and FLAG_SIZE == FLAG_SIZE) {
                radius = visibility * itemSize / 2f
            } else {
                radius = itemSize / 2f
            }
            canvas.drawCircle(
                (itemPadding + itemLength) / 2f,
                itemSize / 2f,
                radius,
                paint
            )
        }
    }

    /**
     * Draw the active indicators. This will be called after [.drawIndicator]
     * to highlight the active item.
     *
     * @param canvas     the canvas to draw to
     * @param progress   the progress of the animation [-1, 1]. Negative values indicate that
     * the previous value is selected and selection is moving to this item.
     * `0` indicates that the item is fully selected, positive values
     * that the selection is moving forward.
     * @param visibility a value [0, 1] indicating an ongoing animation.
     * Use this to scale / fade indicators on the edges.
     */
    protected fun drawActiveIndicator(canvas: Canvas, progress: Float, visibility: Float) {
        paint.setColor(colorFocused)
        if (type == TYPE_LINE) {
            paint.setStrokeWidth(itemSize)
            if (progress >= 0) {
                canvas.drawLine(
                    itemPadding / 2 + (itemLength - itemPadding) * progress,
                    itemSize / 2,
                    itemLength - itemPadding / 2,
                    itemSize / 2,
                    paint
                )
            } else if (progress < 0) {
                canvas.drawLine(
                    itemPadding / 2,
                    itemSize / 2,
                    itemLength - itemPadding / 2 - (itemLength - itemPadding) * (progress + 1),
                    itemSize / 2,
                    paint
                )
            }
        } else if (type == TYPE_CIRCLE) {
            paint.setStyle(Paint.Style.FILL)
            val radius: Float
            if (progress < 0) {
                radius = Math.abs(progress) * itemSize / 2f
            } else {
                radius = (1 - progress) * itemSize / 2f
            }
            canvas.drawCircle(
                (itemPadding + itemLength) / 2f,
                itemSize / 2f,
                radius,
                paint
            )
        }
    }

    fun setItemSize(size: Int) {
        this.itemSize = size.toFloat()
    }

    fun getItemSize(): Float {
        return itemSize
    }

    fun getItemLength(): Float {
        return itemLength
    }

    fun setItemLength(length: Float) {
        this.itemLength = length
    }

    fun getItemPadding(): Float {
        return itemPadding
    }

    fun setItemPadding(padding: Float) {
        this.itemPadding = padding
    }

    fun getMaxDisplayedItems(): Int {
        return maxDisplayedItems
    }

    fun setMaxDisplayedItems(max: Int) {
        this.maxDisplayedItems = max
    }

    fun setEdgeAnimationFlags(flags: Int) {
        edgeAnimationFlags = flags
    }

    fun getEdgeAnimationFlags(): Int {
        return edgeAnimationFlags
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun getType(): Int {
        return type
    }

    fun setColorBackground(colorBackground: Int) {
        this.colorBackground = colorBackground
    }

    fun setColorFocused(colorFocused: Int) {
        this.colorFocused = colorFocused
    }
}