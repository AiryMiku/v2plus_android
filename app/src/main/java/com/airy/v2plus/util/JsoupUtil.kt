package com.airy.v2plus.util

import com.airy.v2plus.bean.custom.LoginKey
import org.jsoup.Jsoup


/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

fun getLoginValue(response: String): LoginKey {
    val body = Jsoup.parse(response)
    val formTableBody = body.getElementsByAttributeValue("action", "/signin").select("tbody").select("tr")
    val userNameValue = formTableBody[0].getElementsByAttributeValue("type", "text").attr("name")
    val pwValue = formTableBody[1].getElementsByAttributeValue("type", "password").attr("name")
    val verifiedCodeInfo = formTableBody[2].select("td")[1].select("div")
    val verifyUrl = verifiedCodeInfo[0].attr("style")
    val verifyValue = formTableBody[2].getElementsByAttributeValue("type", "text").attr("name")
    val onceValue = formTableBody[3].getElementsByAttributeValue("type", "hidden").attr("value")
    return LoginKey(userNameValue, pwValue, "/", onceValue, verifyUrl, verifyValue)
}