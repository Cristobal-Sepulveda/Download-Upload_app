package com.udacity

import android.animation.ValueAnimator
import android.app.NotificationManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.renderscript.Sampler
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.udacity.ButtonState
import com.udacity.R
import com.udacity.sendNotification
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates


/** The JvmOverloads instructs the Kotlin compiler to generate overloads for this function
 * that substitute default parameter values */
class LoadingButton @JvmOverloads constructor(context: Context,
                                              attrs: AttributeSet? = null,
                                              defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val valueAnimator = ValueAnimator()
    private var animatedWidth = 0f

    /** this is set to draw or write things in future, here i set the characteristics of the "stroke
     * from my pincel" */
    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        //stroke means "trazo" in spanish
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    /** Here I'm getting the data from dimens.xml to draw the rectangle*/
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    val circleLeft: Float = width.toFloat() - .13f*width
    val circleTop: Float = height.toFloat() - .71f*height
    val circleRight: Float = circleLeft + 60.0f
    val circleBottom: Float = circleTop + 60.0f
    val sweepAngle: Float = (animatedWidth/width) * 360

    /** Add an offset and a text size for text that is drawn inside the rectangle */
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    /** Color to the button*/
    private val unanimedButtonColorBackground = resources.getColor(R.color.colorPrimary)
    private val animatedButtonColorBackground = resources.getColor(R.color.colorPrimaryDark)

    /** This is for assing a valid attribute to this custom view, i'll use it to start or stop
     * the animation'*/
    var buttonState: ButtonState
            by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
                when (buttonState) {
                    ButtonState.Loading -> setAnimator()
                    ButtonState.Completed -> stopAnimator()
                }
            }


    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = unanimedButtonColorBackground
        canvas.drawRect(clipRectLeft,clipRectTop,clipRectRight,clipRectBottom,paint)
        when (buttonState) {
            ButtonState.Loading -> drawAnimatedButton(canvas)
            ButtonState.Completed -> drawUnanimedButton(canvas)
        }

    }

    /** This method is to drawText on the button*/
    private fun drawUnanimedButton(canvas: Canvas){
        paint.color = Color.WHITE
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            context.getString(R.string.button_initial_text),
            clipRectRight/2, textOffset, paint
        )
    }

    private fun drawAnimatedButton(canvas: Canvas) {
        paint.color = animatedButtonColorBackground
        canvas.drawRect(
                clipRectLeft, clipRectTop,
                animatedWidth, clipRectBottom, paint)

        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(
            context.getString(R.string.button_loading),
            clipRectRight/2, textOffset, paint
        )
        val circleLeft: Float = width.toFloat() - .13f*width
        val circleTop: Float = height.toFloat() - .71f*height
        val circleRight: Float = circleLeft + 60.0f
        val circleBottom: Float = circleTop + 60.0f
        val sweepAngle: Float = (animatedWidth/width) * 360

        paint.color = resources.getColor(R.color.colorAccent)
        canvas.drawArc(
            circleLeft, circleTop, circleRight, circleBottom,
            0F, sweepAngle, true, paint)
    }

    private fun setAnimator() {
        valueAnimator.duration = 3000
        valueAnimator.repeatCount= ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.setFloatValues(0f, width.toFloat())
        valueAnimator.addUpdateListener {
            animatedWidth = it.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()
    }

    private fun stopAnimator() {

        valueAnimator.cancel()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
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
    }
}