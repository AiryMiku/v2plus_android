package com.airy.v2plus.util

import com.airy.v2plus.bean.custom.CellItem
import com.airy.v2plus.bean.custom.LoginResult
import com.airy.v2plus.login.LoginKey
import org.jsoup.Jsoup




/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class JsoupUtil {

    companion object {

        private const val TAG = "JsoupUtil"

        fun getLoginValue(response: String): LoginKey {
            val body = Jsoup.parse(response)
            val formTableBody = body.getElementsByAttributeValue("action", "/signin").select("tbody").select("tr")
            val userNameValue = formTableBody[0].getElementsByAttributeValue("type", "text").attr("name")
            val pwValue = formTableBody[1].getElementsByAttributeValue("type", "password").attr("name")
            val verifyValue = formTableBody[2].getElementsByAttributeValue("type", "text").attr("name")
            val onceValue = formTableBody[3].getElementsByAttributeValue("type", "hidden").attr("value")
            return LoginKey(userNameValue, pwValue, "/", onceValue, "", verifyValue)
        }

        fun getMainPageItems(response: String): List<CellItem> {
            val dataList = ArrayList<CellItem>()
            val doc = Jsoup.parse(response)
            val content = doc.getElementsByClass("cell item")
            for (item in content) {
                val avatar = item.getElementsByClass("avatar").select("img").attr("src")
                val title = item.getElementsByClass("item_title").select("a[href]").text()
                val node = item.getElementsByClass("node").text()
                val topicInfo = item.getElementsByClass("topic_info").select("span").text().split(" • ")
                val commentCount = if (item.getElementsByClass("count_livid").select("a[href]").text().isNullOrBlank()) {
                    "0"
                } else {
                    item.getElementsByClass("count_livid").select("a[href]").text()
                }
                dataList.add(CellItem(avatar, title, node, topicInfo, commentCount))
            }
            return dataList
        }

        /**
         * @return if success, return a list contains like [Airy,6,节点收藏,2,主题收藏,2,特别关注]
         */
        fun getHomePageUserInfo(response: String): List<String> {
            val doc = Jsoup.parse(response)
            val rightBar = doc.getElementById("Rightbar")
            if (rightBar.select("a").eachAttr("href").contains("/signin")) {
                return emptyList()
            }
            val info = rightBar.getElementsByClass("cell").eachText()
            return info.let {
                if (it.size > 6) {
                    it.subList(0, 6)
                } else {
                    emptyList()
                }
            }
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

        /**
         * @return like location.href = '/mission/daily/redeem?once=44064';
         * and pick the once out if not contain, return empty string
         */
        fun getDailyMissionParam(response: String): String {
            val doc = Jsoup.parse(response)
            val main = doc.getElementById("Main")
            val button = main.getElementsByAttributeValueMatching("onclick", "location.href = '/mission/daily/redeem\\?once=[0-9]+';")
            val attr = button.attr("onclick")
            val m = Regex("[0-9]+").find(attr)
            return if (m == null) {
                ""
            } else {
                return m.groupValues.first()
            }
        }
    }
}
