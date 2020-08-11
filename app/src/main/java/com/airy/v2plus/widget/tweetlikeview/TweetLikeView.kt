package com.airy.v2plus.widget.tweetlikeview

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Checkable
import androidx.annotation.ColorInt
import com.airy.v2plus.R
import com.airy.v2plus.TAG
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class TweetLikeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Checkable {

    companion object {
        object State {
            const val HEART_VIEW = 0
            const val CIRCLE_VIEW = 1
            const val RING_VIEW = 2
            const val RING_DOT_HEART_VIEW = 3
            const val DOT_HEART_VIEW = 4
        }

        /**
         * const for view
         */
        @ColorInt
        const val CHECKED_COLOR = (0xffe53a42).toInt()

        @ColorInt
        const val DEFAULT_COLOR = (0xff657487).toInt()

        @ColorInt
        const val DEFAULT_RING_COLOR = (0xffde7bcc).toInt()

        const val DEFAULT_CYCLE_TIME = 2000
        const val DEFAULT_UN_SELECT_CYCLE_TIME = 200

        val DEFAULT_DOT_COLORS = arrayOf(
            (0xffdaa9fa).toInt(),
            (0xfff2bf4b).toInt(),
            (0xffe3bca6).toInt(),
            (0xff329aed).toInt(),
            (0xffb1eb99).toInt(),
            (0xff67c9ad).toInt(),
            (0xffde6bac).toInt()
        )

        const val RADIUS_INNER_SHAPE_SCALE = 6
        const val DOT_SIZE_SCALE = 7
        const val RING_WIDTH_RATIO = 2f
        const val SIZE_RATIO = 5.2f

    }


    /**
     * custom attrs
     */
    // heart radius
    var mRadius: Float

    // view selected time
    var cycleTime: Int

    // view un selected time
    var unSelectedCycleTime: Int

    @ColorInt
    var defaultColor: Int

    @ColorInt
    var checkedColor: Int

    @ColorInt
    var ringColor: Int

    var lrGroupCRation: Float

    var lrGroupBRation: Float

    var bGroupACRation: Float

    var tGroupBRation: Float

    var innerShapeScale: Int

    var dotSizeScale: Int

    var isRandomDotColor: Boolean = true

    var defaultIcon: Drawable? = null

    var checkedIcon: Drawable? = null

    private var likeChecked: Boolean = false

    /**
     * view attrs
     */
    private var cX: Float

    private var cY: Float

    private val paint = Paint()

    private var animatorTime: ValueAnimator? = null

    private var animatorArgb: ValueAnimator? = null

    private var unSelectedAnimator: ObjectAnimator? = null

    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private var curRadius: Int

    private var curColor: Int

    private var curState: Int = State.HEART_VIEW

    private var dotColors = DEFAULT_DOT_COLORS

    private var shapePath: HeartShapePath? = null

    /**
     * for ring
     */
    private var curPercent: Float = 0f
    private var dotR: Float
    private var rDotL = 0f
    private var rDotS = 0f
    private var offS = 0f
    private var offL = 0f
    private var isMax = false


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TweetLikeView,
            defStyleAttr, 0
        ).apply {

            try {
                mRadius = getDimension(R.styleable.TweetLikeView_cirRadius, dp2px(10))
                cycleTime = getInt(R.styleable.TweetLikeView_cycleTime, DEFAULT_CYCLE_TIME)
                unSelectedCycleTime = getInt(
                    R.styleable.TweetLikeView_unSelectCycleTime,
                    DEFAULT_UN_SELECT_CYCLE_TIME
                )
                defaultColor = getColor(R.styleable.TweetLikeView_defaultColor, DEFAULT_COLOR)
                checkedColor = getColor(R.styleable.TweetLikeView_checkedColor, CHECKED_COLOR)
                ringColor = getColor(R.styleable.TweetLikeView_ringColor, DEFAULT_RING_COLOR)
                lrGroupCRation = getFloat(
                    R.styleable.TweetLikeView_lrGroupCRatio,
                    HeartShapePath.LR_GROUP_C_RATIO
                )
                lrGroupBRation = getFloat(
                    R.styleable.TweetLikeView_lrGroupBRatio,
                    HeartShapePath.LR_GROUP_B_RATIO
                )
                bGroupACRation = getFloat(
                    R.styleable.TweetLikeView_bGroupACRatio,
                    HeartShapePath.B_GROUP_AC_RATIO
                )
                tGroupBRation =
                    getFloat(R.styleable.TweetLikeView_tGroupBRatio, HeartShapePath.T_GROUP_B_RATIO)
                innerShapeScale =
                    getInteger(R.styleable.TweetLikeView_innerShapeScale, RADIUS_INNER_SHAPE_SCALE)
                dotSizeScale = getInteger(R.styleable.TweetLikeView_dotSizeScale, DOT_SIZE_SCALE)
                isRandomDotColor = getBoolean(R.styleable.TweetLikeView_allowRandomDotColor, true)

                if (hasValue(R.styleable.TweetLikeView_defaultLikeIconRes)) {
                    defaultIcon = getDrawable(R.styleable.TweetLikeView_defaultLikeIconRes)
                }

                if (hasValue(R.styleable.TweetLikeView_checkedLikeIconRes)) {
                    checkedIcon = getDrawable(R.styleable.TweetLikeView_checkedLikeIconRes)
                }

                cX = mRadius
                cY = mRadius
                curRadius = mRadius.toInt()
                curColor = defaultColor
                dotR = mRadius / dotSizeScale

            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.translate(cX, cY)
            when (curState) {
                State.HEART_VIEW -> {
                    drawInnerShape(canvas, curRadius, isChecked)
                }
                State.CIRCLE_VIEW -> {
                    drawCircle(canvas, curRadius, curColor)
                }
                State.RING_VIEW -> {
                    drawRing(canvas, curRadius, curColor, curPercent)
                }
                State.RING_DOT_HEART_VIEW -> {
                    drawDotWithRing(canvas, curRadius, curColor)
                }
                State.DOT_HEART_VIEW -> {
                    drawDot(canvas, curRadius, curColor)
                }
            }
        }
    }

    private fun hasIcon() = checkedIcon != null && defaultIcon != null

    // draw heart shape
    private fun drawInnerShape(canvas: Canvas, radius: Int, isChecked: Boolean) {
        if (hasIcon()) {
            val icon = if (isChecked) {
                checkedIcon
            } else {
                defaultIcon
            }
            icon?.setBounds(-radius, -radius, radius, radius)
            icon?.draw(canvas)
        } else {
            val color = if (isChecked) {
                checkedColor
            } else {
                curColor
            }
            with(paint) {
                this.color = color
                isAntiAlias = true
                isDither = true
                style = Paint.Style.FILL
            }
            shapePath =
                HeartShapePath(lrGroupCRation, lrGroupBRation, bGroupACRation, tGroupBRation)
            shapePath?.let {
                canvas.drawPath(it.getPath(radius.toFloat()), paint)
            }
        }
    }

    private fun drawCircle(canvas: Canvas, radius: Int, color: Int) {
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(0f, 0f, radius.toFloat(), paint)
    }

    private fun drawRing(canvas: Canvas, radius: Int, color: Int, percent: Float) {
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeWidth = RING_WIDTH_RATIO * mRadius * percent
        @Suppress("NAME_SHADOWING")
        val radius = radius.toFloat()
        val rectF = RectF(-radius, -radius, radius, radius)
        canvas.drawArc(rectF, 0f, 360f, false, paint)
    }

    private fun drawDotWithRing(canvas: Canvas, radius: Int, color: Int) {
        dotR = mRadius / dotSizeScale
        paint.color = color
        paint.isAntiAlias = true
        val ringPercent = if (1f - curPercent > 1f) {
            1f * 0.2f
        } else {
            (1f - curPercent) * 0.2f
        }
        val ringWidth = RING_WIDTH_RATIO * mRadius * ringPercent
        paint.strokeWidth = ringWidth
        paint.style = Paint.Style.STROKE
        if (curPercent <= 1f) {
            @Suppress("NAME_SHADOWING")
            val radius = radius.toFloat()
            val rectF = RectF(-radius, -radius, radius, radius)
            canvas.drawArc(rectF, 0f, 360f, false, paint)
        }

        // ring center
        val innerR = radius - ringWidth / 2 + dotR
        var angleA = 0.0
        var angleB = -Math.PI / 20
        if (rDotL <= SIZE_RATIO * mRadius / 2) {
            offS += dotR / 17
            offL += dotR / 14
            rDotS = radius - mRadius / 12 / 2 + offS
            rDotL = innerR + offL
        }

        paint.style = Paint.Style.FILL
        for (i in 0 until 7) {
            canvas.drawCircle(
                (rDotS * sin(angleA)).toFloat(),
                (rDotS * cos(angleA)).toFloat(),
                dotR,
                paint
            )
            angleA += 2 * PI / 7
            canvas.drawCircle(
                (rDotL * sin(angleB)).toFloat(),
                (rDotL * cos(angleB)).toFloat(),
                dotR,
                paint
            )
            angleB += 2 * PI / 7
        }
        curRadius =
            (mRadius / innerShapeScale + (innerShapeScale * 2 - 2) * mRadius * curPercent / innerShapeScale).toInt()
        drawInnerShape(canvas, curRadius, true)
    }

    private fun drawDot(canvas: Canvas, radius: Int, @ColorInt color: Int) {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL

        var angleA = 0.0
        var angleB = -PI / 20
        var dotRS = 0f
        var dotRL = 0f
        if (rDotL <= SIZE_RATIO * mRadius / 2) {
            rDotS += dotR / 17
            rDotL += dotR / 14
        }
        if (!isMax && curRadius <= 1.1 * mRadius) {
            offL += dotR / 14;
            curRadius = (mRadius / 3 + offL * 4).toInt()
        } else {
            isMax = true
        }

        if (isMax && curRadius > mRadius) {
            curRadius = (curRadius - dotR / 16).toInt()
        }
        drawInnerShape(canvas, curRadius, true)

        dotRS = dotR * (1 - curPercent)
        dotRL = if (dotR * (1 - curPercent) * 3 > dotR) dotR else (dotR * (1 - curPercent)) * 2
        for (i in dotColors.indices) {
            paint.color = dotColors[i]
            canvas.drawCircle(
                (rDotS * sin(angleA)).toFloat(),
                (rDotS * cos(angleA)).toFloat(),
                dotRS,
                paint
            )
            angleA += 2 * PI / 7
            canvas.drawCircle(
                (rDotL * sin(angleB)).toFloat(),
                (rDotL * cos(angleB)).toFloat(),
                dotRL,
                paint
            )
            angleB += 2 * PI / 7
        }
    }

    private fun randomDotColors() {
        val seed = dotColors.size
        for (i in dotColors.indices) {
            val r = Random.nextInt(seed)
            val curColor = dotColors[i]
            dotColors[i] = dotColors[r]
            dotColors[r] = curColor
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cX = w / 2f
        cY = h / 2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthOrHeight = (SIZE_RATIO * mRadius + 2 * dotR).toInt()
        setMeasuredDimension(widthOrHeight, widthOrHeight)
    }

    private fun resetState() {
        curPercent = 0f
        curRadius = 0
        isMax = false
        rDotS = 0f
        rDotL = 0f
        offS = 0f
        offL = 0f
    }

    private fun calcPercent(start: Float, end: Float, current: Float): Float {
        return (current - start) / (end - start)
    }

    private fun restoreDefaultView() {
        curColor = defaultColor
        curRadius = mRadius.toInt()
        curState = State.HEART_VIEW
        invalidate()
    }

    private fun restoreDefaultViewChecked() {
        curColor = checkedColor
        curRadius = mRadius.toInt()
        curState = State.HEART_VIEW
        invalidate()
    }

    private fun cancelAnimator() {
        if (isAnimatorTimeRunning()) {
            animatorTime?.cancel()
        }
    }

    private fun isAnimatorTimeRunning(): Boolean = animatorTime != null && animatorTime!!.isRunning

    private fun startSelectViewMotion() {
        resetState()
        if (isRandomDotColor) {
            randomDotColors()
        }
        if (animatorTime == null) {
            animatorTime = ValueAnimator.ofInt(0, 1200).apply {
                duration = cycleTime.toLong()
                interpolator = LinearInterpolator()
            }

        }
        if (animatorUpdateListener == null) {
            animatorUpdateListener = LvAnimatorUpdateListener()
            animatorTime?.addUpdateListener(animatorUpdateListener)
        }
        animatorTime?.start()
    }

    private fun startUnselectedViewMotion() {
        if (unSelectedAnimator == null) {
            val holderX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.8f, 1.0f)
            val holderY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.8f, 1.0f)
            unSelectedAnimator =
                ObjectAnimator.ofPropertyValuesHolder(this, holderX, holderY).apply {
                    duration = unSelectedCycleTime.toLong()
                    interpolator = OvershootInterpolator()
                }

        }
        unSelectedAnimator?.start()
    }

    private fun selectLike(isSetChecked: Boolean) {
        likeChecked = isSetChecked
        if (isSetChecked) {
            cancelAnimator()
            startSelectViewMotion()
        } else {
            if (!isAnimatorTimeRunning()) {
                restoreDefaultView()
                startUnselectedViewMotion()
            }
        }
    }

    private fun selectLikeViewWithoutAnimator(isSetChecked: Boolean) {
        likeChecked = isSetChecked
        cancelAnimator()
        if (isSetChecked) {
            restoreDefaultViewChecked()
        } else {
            restoreDefaultView()
        }
    }

    private fun releaseAnimator(animator: ValueAnimator?) {
        animator?.end()
        animator?.removeAllListeners()
        animator?.removeAllUpdateListeners()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        releaseAnimator(animatorTime)
        releaseAnimator(animatorArgb)
        releaseAnimator(unSelectedAnimator)
        animatorUpdateListener = null
    }

    /**
     * check
     */
    override fun isChecked(): Boolean {
        return likeChecked
    }

    override fun toggle() {
        selectLike(!likeChecked)
    }

    override fun setChecked(checked: Boolean) {
        selectLike(checked)
    }

    /**
     * public method
     */
    fun toggleWithoutAnimator() {
        selectLikeViewWithoutAnimator(!likeChecked)
    }

    /**
     * util
     */
    private fun dp2px(value: Int): Float {
        return TypedValue
            .applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),
                resources.displayMetrics
            )
    }

    private inner class LvAnimatorUpdateListener : ValueAnimator.AnimatorUpdateListener {
        override fun onAnimationUpdate(animation: ValueAnimator?) {
            val animatedValue = animation?.animatedValue as Int
            if (animatedValue == 0) {
                if (animatorArgb == null || !animatorArgb!!.isRunning) {
                    animatorArgb =
                        ValueAnimator.ofArgb(defaultColor, 0xf74769, checkedColor).apply {
                            duration = (cycleTime * 28 / 120).toLong()
                            interpolator = LinearInterpolator()
                            start()
                        }
                }
            } else if (animatedValue <= 100) {
                val percent = calcPercent(0f, 100f, animatedValue.toFloat())
                curRadius = (mRadius - mRadius * percent).toInt()
                if (animatorArgb != null && animatorArgb!!.isRunning) {
                    curColor = animatorArgb!!.animatedValue as Int
                }
                curState = State.HEART_VIEW
                invalidate()

            } else if (animatedValue <= 280) {
                val percent = calcPercent(100f, 340f, animatedValue.toFloat())
                curRadius = (2 * mRadius * percent).toInt()
                if (animatorArgb != null && animatorArgb!!.isRunning) {
                    curColor = animatorArgb!!.animatedValue as Int
                }
                curState = State.CIRCLE_VIEW
                invalidate()

            } else if (animatedValue <= 340) {
                val percent = calcPercent(100f, 340f, animatedValue.toFloat())
                curPercent = if (1f - percent + 0.2f > 1f) 1f else 1f - percent + 0.2f
                if (animatorArgb != null && animatorArgb!!.isRunning) {
                    curColor = animatorArgb!!.animatedValue as Int
                }
                curState = State.RING_VIEW
                invalidate()

            } else if (animatedValue <= 480) {
                val percent = calcPercent(340f, 480f, animatedValue.toFloat())
                curPercent = percent
                curRadius = (2 * mRadius).toInt()
                curState = State.RING_DOT_HEART_VIEW
                invalidate()

            } else if (animatedValue <= 1200) {
                val percent = calcPercent(480f, 1200f, animatedValue.toFloat())
                curPercent = percent
                curState = State.DOT_HEART_VIEW
                invalidate()

                if (animatedValue == 1200) {
                    animatorTime?.cancel()
                    if (!isChecked) {
                        restoreDefaultView()
                    } else {
                        restoreDefaultViewChecked()
                    }
                }
            }
        }

    }
}

