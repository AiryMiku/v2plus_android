package com.airy.v2plus.widget.tweetlikeview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.Checkable
import androidx.annotation.ColorInt
import com.airy.v2plus.R
import kotlin.properties.Delegates


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
    private var radius: Float

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

//    private var lrGroupCRation: Float


    init {
        context.
        theme.
        obtainStyledAttributes(attrs,
            R.styleable.TweetLikeView,
            defStyleAttr, 0).apply {

            try {
                radius = getDimension(R.styleable.TweetLikeView_cirRadius, dp2px(10))
                cycleTime = getInt(R.styleable.TweetLikeView_cycleTime, DEFAULT_CYCLE_TIME)
                unSelectedCycleTime = getInt(R.styleable.TweetLikeView_unSelectCycleTime, DEFAULT_UN_SELECT_CYCLE_TIME)
                defaultColor = getColor(R.styleable.TweetLikeView_defaultColor, DEFAULT_COLOR)
                checkedColor = getColor(R.styleable.TweetLikeView_checkedColor, CHECKED_COLOR)
                ringColor = getColor(R.styleable.TweetLikeView_ringColor, DEFAULT_RING_COLOR)
//                lrGroupCRation = getFloat(R.styleable.TweetLikeView_lrGroupCRatio, )

            } finally {
                recycle()
            }
        }


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