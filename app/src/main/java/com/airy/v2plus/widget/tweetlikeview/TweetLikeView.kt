package com.airy.v2plus.widget.tweetlikeview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Checkable
import androidx.annotation.ColorInt
import com.airy.v2plus.R


/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class TweetLikeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ): View(context, attrs, defStyleAttr), Checkable {

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
        const val CHECKED_COLOR = 0xe53a42
        @ColorInt
        const val DEFAULT_COLOR = 0x657487
        @ColorInt
        const val DEFAULT_RING_COLOR = 0xde7bcc

        const val DEFAULT_CYCLE_TIME = 2000
        const val DEFAULT_UN_SELECT_CYCLE_TIME = 200

        val DEFAULT_DOT_COLORS = arrayOf(0xdaa9fa, 0xf2bf4b, 0xe3bca6, 0x329aed,
            0xb1eb99, 0x67c9ad, 0xde6bac)

        const val RADIUS_INNER_SHAPE_SCALE = 6
        const val DOT_SIZE_SCALE = 7
        const val RING_WIDTH_RATIO = 2f
        const val SIZE_RATIO = 5.2f

    }


    /**
     * custom attrs
     */
    // heart radius
    private var mRadius: Float

    // view selected time
    private var cycleTime: Int

    // view un selected time
    private var unSelectedCycleTime: Int

    @ColorInt
    private var defaultColor: Int

    @ColorInt
    private var checkedColor: Int

    @ColorInt
    private var ringColor: Int

    private var lrGroupCRation: Float

    private var lrGroupBRation: Float

    private var bGroupACRation: Float

    private var tGroupBRation: Float

    private var innerShapeScale: Int

    private var dotSizeScale: Int

    private var isRandomDotColor: Boolean

    private var defaultIcon: Drawable? = null

    private var checkedIcon: Drawable? = null

    /**
     * view attrs
     */
    private var cX: Float

    private var cY: Float

    private val paint by lazy { Paint() }

    private var animatorTime: ValueAnimator? = null

    private var animatorArgb: ValueAnimator? = null

    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private var curRadius: Int

    private var curColor: Int

    private var curState: Int? = null

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
        context.
        theme.
        obtainStyledAttributes(attrs,
            R.styleable.TweetLikeView,
            defStyleAttr, 0).apply {

            try {
                mRadius = getDimension(R.styleable.TweetLikeView_cirRadius, dp2px(10))
                cycleTime = getInt(R.styleable.TweetLikeView_cycleTime, DEFAULT_CYCLE_TIME)
                unSelectedCycleTime = getInt(R.styleable.TweetLikeView_unSelectCycleTime, DEFAULT_UN_SELECT_CYCLE_TIME)
                defaultColor = getColor(R.styleable.TweetLikeView_defaultColor, DEFAULT_COLOR)
                checkedColor = getColor(R.styleable.TweetLikeView_checkedColor, CHECKED_COLOR)
                ringColor = getColor(R.styleable.TweetLikeView_ringColor, DEFAULT_RING_COLOR)
                lrGroupCRation = getFloat(R.styleable.TweetLikeView_lrGroupCRatio, HeartShapePath.LR_GROUP_C_RATIO)
                lrGroupBRation = getFloat(R.styleable.TweetLikeView_lrGroupBRatio, HeartShapePath.LR_GROUP_B_RATIO)
                bGroupACRation = getFloat(R.styleable.TweetLikeView_bGroupACRatio, HeartShapePath.B_GROUP_AC_RATIO)
                tGroupBRation = getFloat(R.styleable.TweetLikeView_tGroupBRatio, HeartShapePath.T_GROUP_B_RATIO)
                innerShapeScale = getInteger(R.styleable.TweetLikeView_innerShapeScale, RADIUS_INNER_SHAPE_SCALE)
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
        curState?.let { state ->
            canvas?.let {
                when(state) {
//                State.HEART_VIEW
                    else -> {}
                }
            }
        }
    }

    private fun hasIcon() = checkedIcon != null && defaultIcon != null

    // draw heart shape
    private fun drawInnerShape(canvas: Canvas, radius: Int, isChecked: Boolean) {
        if (hasIcon()) {
            val icon = if (isChecked) { checkedIcon } else { defaultIcon }
            icon?.setBounds(-radius, -radius, radius, radius)
            icon?.draw(canvas)
        } else {
            val color = if (isChecked) { checkedColor } else { curColor }
            paint.color = color
            paint.isAntiAlias = true
            paint.isDither = true
            paint.style = Paint.Style.FILL
            shapePath = HeartShapePath(lrGroupCRation, lrGroupBRation, bGroupACRation, tGroupBRation)
            canvas.drawPath(shapePath!!.getPath(radius.toFloat()), paint)
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
        val innerR = radius - ringWidth/2 + dotR
        val angleA = 0f
        val angleB = -Math.PI / 20
        if (rDotL <= SIZE_RATIO * mRadius/2) {
            offS += dotR / 17
            offL += dotR / 14
            rDotS = radius - mRadius / 12 / 2 + offS
            rDotL = innerR + offL
        }

        paint.style = Paint.Style.FILL

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // todo release animator
    }

    private fun releaseAnimator(animator: ValueAnimator){
        animator.end()
        animator.removeAllListeners()
        animator.removeAllUpdateListeners()
    }

    /**
     * check
     */
    override fun isChecked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun toggle() {
        TODO("Not yet implemented")
    }

    override fun setChecked(checked: Boolean) {
        TODO("Not yet implemented")
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
}