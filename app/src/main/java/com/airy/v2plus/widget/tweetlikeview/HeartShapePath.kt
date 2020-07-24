package com.airy.v2plus.widget.tweetlikeview

/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class HeartShapePath(
    lrGroupCRation: Float,
    lrGroupBRation: Float,
    bGroupLRRation: Float,
    tGroupBRation: Float) {

    companion object {
        const val BEZIER_C = 0.551915024494f
        const val LR_GROUP_C_RATIO = 0.92f
        const val LR_GROUP_B_RATIO = 1.0f
        const val B_GROUP_AC_RATIO =0.7f
        const val T_GROUP_B_RATIO =0.5f
    }
}