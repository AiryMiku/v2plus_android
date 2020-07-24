package com.airy.v2plus.widget.tweetlikeview

import android.content.Context
import kotlin.properties.Delegates

/**
 * Created by Airy on 2020/7/24
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class LikeViewBuilder(context: Context){

    private var defaultColor by Delegates.notNull<Int>()

    init {
        defaultColor = 1
    }
}