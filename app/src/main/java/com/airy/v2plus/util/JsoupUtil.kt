package com.airy.v2plus.util

import com.airy.v2plus.bean.custom.LoginResult
import com.airy.v2plus.bean.custom.PageCell
import com.airy.v2plus.login.LoginKey
import org.jsoup.Jsoup




/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class JsoupUtil {
    companion object {

        fun getLoginValue(response: String): LoginKey {
            val body = Jsoup.parse(response)
            val formTableBody = body.getElementsByAttributeValue("action", "/signin").select("tbody").select("tr")
            val userNameValue = formTableBody[0].getElementsByAttributeValue("type", "text").attr("name")
            val pwValue = formTableBody[1].getElementsByAttributeValue("type", "password").attr("name")
            val verifyValue = formTableBody[2].getElementsByAttributeValue("type", "text").attr("name")
            val onceValue = formTableBody[3].getElementsByAttributeValue("type", "hidden").attr("value")
            return LoginKey(userNameValue, pwValue, "/", onceValue, "", verifyValue)
        }

        fun getMainPage(response: String): List<PageCell> {
            val dataList = ArrayList<PageCell>()
            val doc = Jsoup.parse(response)
            val content = doc.getElementsByClass("cell item")
            for (item in content) {
                val title = item.getElementsByClass("item_title").select("a[href]").text()
                val node = item.getElementsByClass("node").text()
                val topicInfo = item.getElementsByClass("topic_info").select("span").text().split(" • ")
                val commentCount = if (item.getElementsByClass("count_livid").select("a[href]").text().isNullOrBlank()) {
                    "0"
                } else {
                    item.getElementsByClass("count_livid").select("a[href]").text()
                }
                dataList.add(PageCell(title, node, topicInfo, commentCount))
            }
            return dataList
        }

        fun getLoginResult(response: String): LoginResult {
            val doc = Jsoup.parse(response)
            val problems = doc.getElementsByClass("problem").select("li").eachText()
            val td = doc.select("div#Rightbar").select("div.box")[0].getElementsByTag("td").first()
            val userName = if (td != null ) {
                td.getElementsByTag("a")[0].attr("href").replace("/member/", "")
            } else {
                ""
            }
            val avatarUrl = if (td != null) {
                "http:" + td.getElementsByTag("img")[0].attr("src")
            } else {
                ""
            }
            return LoginResult(problems, userName, avatarUrl)
        }
    }
}
