package com.airy.v2plus.widget.tweetlikeview

import android.content.Context
import androidx.annotation.ColorInt
import kotlin.properties.Delegates

/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class LikeViewBuilder(private val context: Context){

    @ColorInt
    var defaultColor = TweetLikeView.DEFAULT_COLOR

    @ColorInt
    var checkedColor = TweetLikeView.CHECKED_COLOR

    var lrGroupCRatio = HeartShapePath.LR_GROUP_C_RATIO

    var lrGroupBRatio = HeartShapePath.LR_GROUP_B_RATIO

    var bGroupACRation = HeartShapePath.B_GROUP_AC_RATIO

    var tGroupBRation = HeartShapePath.T_GROUP_B_RATIO

    var radius = 30

    var cycleTime = TweetLikeView.DEFAULT_CYCLE_TIME

    var unSelectCycleTime = TweetLikeView.DEFAULT_UN_SELECT_CYCLE_TIME

    var dotColors = TweetLikeView.DEFAULT_DOT_COLORS

    var ringColor = TweetLikeView.DEFAULT_RING_COLOR

    var innerShapeScale = TweetLikeView.RADIUS_INNER_SHAPE_SCALE

    var dotSizeScale = TweetLikeView.DOT_SIZE_SCALE

    var isRandomDotColor = true

    fun create(): TweetLikeView = TweetLikeView(context).apply {

    }
}