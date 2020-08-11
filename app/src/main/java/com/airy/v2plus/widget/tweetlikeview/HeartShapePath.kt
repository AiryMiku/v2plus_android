package com.airy.v2plus.widget.tweetlikeview

import android.graphics.Path
import android.graphics.PointF




/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class HeartShapePath(
    private val lrGroupCRation: Float,
    private val lrGroupBRation: Float,
    private val bGroupLRRation: Float,
    private val tGroupBRation: Float) {

    companion object {
        const val BEZIER_C = 0.551915024494f
        const val LR_GROUP_C_RATIO = 0.92f
        const val LR_GROUP_B_RATIO = 1.0f
        const val B_GROUP_AC_RATIO = 0.7f
        const val T_GROUP_B_RATIO = 0.5f
    }

    private var tPointA: PointF? = null
    private var tPointB: PointF? = null
    private var tPointC: PointF? = null
    private var rPointA: PointF? = null
    private var rPointB: PointF? = null
    private var rPointC: PointF? = null
    private var bPointA: PointF? = null
    private var bPointB: PointF? = null
    private var bPointC: PointF? = null
    private var lPointA: PointF? = null
    private var lPointB: PointF? = null
    private var lPointC: PointF? = null

    private fun updateControlPoints(radius: Float) {
        val offset = BEZIER_C * radius

        // 一组三个控制点,四个方向控制心形绘制
        tPointA = PointF(-offset, -radius)
        tPointB = PointF(0f, -radius * tGroupBRation)
        tPointC = PointF(offset, -radius)

        rPointA = PointF(radius, -offset)
        rPointB = PointF(radius * lrGroupBRation, 0f)
        rPointC = PointF(radius * lrGroupCRation, offset)

        bPointA = PointF(-offset, radius * bGroupLRRation)
        bPointB = PointF(0f, radius)
        bPointC = PointF(offset, radius * bGroupLRRation)

        lPointA = PointF(-radius, -offset)
        lPointB = PointF(-radius * lrGroupBRation, 0f)
        lPointC = PointF(-radius * lrGroupCRation, offset)
    }

    fun getPath(radius: Float): Path {
        updateControlPoints(radius)
        return Path().apply {
            moveTo(tPointB!!.x, tPointB!!.y)
            cubicTo(tPointC!!.x, tPointC!!.y, rPointA!!.x, rPointA!!.y, rPointB!!.x, rPointB!!.y)
            cubicTo(rPointC!!.x, rPointC!!.y, bPointC!!.x, bPointC!!.y, bPointB!!.x, bPointB!!.y)
            cubicTo(bPointA!!.x, bPointA!!.y, lPointC!!.x, lPointC!!.y, lPointB!!.x, lPointB!!.y)
            cubicTo(lPointA!!.x, lPointA!!.y, tPointA!!.x, tPointA!!.y, tPointB!!.x, tPointB!!.y)
        }
    }
}