package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

/** The JvmOverloads instructs the Kotlin compiler to generate overloads for this function
 * that substitute default parameter values */
class LoadingButton @JvmOverloads constructor(context: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val valueAnimator = ValueAnimator()
    private var buttonState: ButtonState
    by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        //stroke means "trazo" in spanish
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    /** Here I'm getting the data from dimens.xml*/
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    /** Add an offset and a text size for text that is drawn inside the rectangle */
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    /** Color to the button*/
    private val buttonColorBackground = resources.getColor(R.color.colorPrimary)



    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawClippedRectangle(canvas)

    }

/*    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }*/

    private fun drawClippedRectangle(canvas: Canvas) {

        //TODO 2.2
        canvas.clipRect(clipRectLeft,clipRectTop,
                        clipRectRight,clipRectBottom)

        //TODO 2.3
        canvas.drawColor(buttonColorBackground)

        //TODO 2.6
        paint.color = Color.WHITE
        // Align the RIGHT side of the text with the origin.
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
                context.getString(R.string.button_initial_text),
                670F, textOffset, paint
        )
    }
}