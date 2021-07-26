package com.airy.v2plus.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.ViewCompat
import com.airy.v2plus.R
import kotlin.math.abs
import kotlin.math.max

/**
 * Created by Airy on 2021/7/26
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class JikeAvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    companion object {
        const val TAG = "JikeAvatarImageView"

        private const val TAG_ID = R.id.app_logo
        private const val TAG_FOLLOW_ID = R.id.app_logo_shadow

        private const val RESUME_ANIMATOR_DURATION = 700L
        private const val FOLLOW_ANIMATOR_DURATION = 80L
        private const val FOLLOW_ANIMATOR_START_DELAY = FOLLOW_ANIMATOR_DURATION - 10L
    }

    private val downEventPoint = PointF()
    private val upEventPoint = PointF()
    private val downRawPoint = PointF()
    private val layoutPoint = PointF()

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var isMoved = false

    var followingItemViews: ArrayList<View>? = null
    var enableFeedback = true
        set(value) {
            field = value
            isHapticFeedbackEnabled = value
            isSoundEffectsEnabled = value
        }

//    init {
//        followingItemViews = ArrayList()
//
//        for (i in 0..4) {
//            val imageView = AppCompatImageView(context, attrs).apply {
//                alpha = 0.8f
//            }
//            Log.d(TAG, "im: ${imageView.drawable}")
//            followingItemViews?.add(imageView)
//        }
//    }

    private val hasFollowingViews: Boolean get() = followingItemViews?.isNotEmpty() ?: false

    private fun filterMotionEvent(event: MotionEvent) = max(abs(event.rawX - downRawPoint.x), abs( event.rawY - downRawPoint.y)) > touchSlop

    private val isRunningAnimator get() = (getTag(TAG_ID) as? Animator)?.isRunning ?: false

    private fun createAnimator(
        target: View,
        propertyName: String,
        start: Float,
        end: Float
    ) = ObjectAnimator.ofFloat(target, propertyName, start, end)

    private fun startFollowingAnimator(
        propertyName: String,
        start: Float,
        end: Float
    ) {
        if (!ViewCompat.isAttachedToWindow(this)) {
            return
        }

        val followViews = this.followingItemViews ?: return
        if (!hasFollowingViews) {
            return
        }

        val animatorItems = followViews.map {
            it.visibility = VISIBLE
            createAnimator(it, propertyName, start, end)
        }
        val oldAnimator = getTag(TAG_FOLLOW_ID) as? Animator
        val animatorSet = AnimatorSet().apply {
            playSequentially(animatorItems)
            interpolator = LinearInterpolator()
            duration = FOLLOW_ANIMATOR_DURATION
            startDelay = FOLLOW_ANIMATOR_START_DELAY
            start()
            if (oldAnimator != null) {
                doOnStart { oldAnimator.cancel() }
            }
        }
        setTag(TAG_FOLLOW_ID, animatorSet)
    }

    private fun startReleaseAnimator(startPoint: PointF, endPoint: PointF) {
        clearAnimation()

        val x = createAnimator(this, "x", startPoint.x, endPoint.x)
        val y = createAnimator(this, "y", startPoint.y, endPoint.y)
        val animatorSet = AnimatorSet().apply {
            playTogether(x, y)
            duration = RESUME_ANIMATOR_DURATION
            interpolator = OvershootInterpolator(1.9f)
            start()
        }
        val followingViews = this.followingItemViews
        if (followingViews != null && hasFollowingViews) {
            animatorSet.doOnEnd {
                postDelayed({
                    followingViews.forEach { it.visibility = INVISIBLE }
                }, FOLLOW_ANIMATOR_DURATION * followingViews.size)
            }
        }
        setTag(TAG_ID, animatorSet)
    }

    private fun cleanAnimatorSets() {
        (getTag(TAG_ID) as? Animator)?.cancel()
        (getTag(TAG_FOLLOW_ID) as? Animator)?.cancel()
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        layoutPoint.set(this.x, this.y)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isRunningAnimator) {
            return super.onTouchEvent(event)
        }
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // located the down point opposite the
                downEventPoint.set(event.x, event.y)
                downRawPoint.set(event.rawX, event.rawY)
                cleanAnimatorSets()
                playSoundEffect(SoundEffectConstants.CLICK)
                isMoved = false
                super.onTouchEvent(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                // add the move dx and dy
                val dx = event.x - downEventPoint.x
                val dy = event.y - downEventPoint.y

                if (!isMoved && filterMotionEvent(event)) {
                    isMoved = true
                    isPressed = false
                }

                this.x += dx
                this.y += dy

                performHapticFeedback(
                    HapticFeedbackConstants.CLOCK_TICK,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                return true
            }

            MotionEvent.ACTION_UP -> {
                upEventPoint.set(this.x, this.y)

                startReleaseAnimator(upEventPoint, layoutPoint)
                playSoundEffect(SoundEffectConstants.CLICK)

                // if moving, just ignore the pressed
                if (isMoved) {
                    return false
                }
            }
        }
        return super.onTouchEvent(event)
    }

    @Keep
    override fun setX(x: Float) {
        startFollowingAnimator("x", this.x, x)
        super.setX(x)
    }

    @Keep
    override fun setY(y: Float) {
        startFollowingAnimator("y", this.y, y)
        super.setY(y)
    }

    override fun onDetachedFromWindow() {
        cleanAnimatorSets()
        super.onDetachedFromWindow()
    }
}