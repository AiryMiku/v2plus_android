package com.airy.v2plus

import android.os.Build


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

object Config {

    const val BASE_URL = "https://www.v2ex.com"
    const val BASE_API_URL = "$BASE_URL/api/"

    const val USER_AGENT = "V2EX+/" + BuildConfig.VERSION_NAME
    val USER_AGENT_ANDROID = String.format("%s (Android %s)", USER_AGENT, Build.VERSION.RELEASE)

}
