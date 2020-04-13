package com.airy.v2plus.widget

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs
import kotlin.math.max

class ZoomOutPageTransformer: ViewPager.PageTransformer {

    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f



    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        if (position < -1) {
            page.alpha = 0f
        } else if (position <= 1) {
            val scaleFactor = max(MIN_SCALE, 1 - abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor)/2
            val horzMargin = pageWidth * (1 - scaleFactor)/2
            if (position < 0) {
                page.translationX = horzMargin - vertMargin/2
            } else {
                page.translationX = -horzMargin + vertMargin/2
            }

            page.scaleX = scaleFactor
            page.scaleY = scaleFactor

            page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE)/(1 - MIN_SCALE) * (1 - MIN_ALPHA)
        } else {
            page.alpha = 0f
        }
    }
}