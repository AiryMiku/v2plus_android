package com.airy.v2plus.util

import com.airy.v2plus.bean.custom.*
import com.airy.v2plus.ui.login.LoginKey
import org.jsoup.Jsoup




/**
 * Created by Airy on 2019-08-31
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class V2exHtmlUtil {

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

        fun getMainPageItems(response: String): List<MainPageItem> {
            val dataList = ArrayList<MainPageItem>()
            val doc = Jsoup.parse(response)
            val content = doc.getElementsByClass("cell item")
            for (item in content) {
                val avatar = item.getElementsByClass("avatar").select("img").attr("src")
                val titleLink = item.getElementsByClass("item_title").select("a[href]")
                val title = titleLink.text()
                var topicId = 0L
                val m = Regex("[0-9]+").find(titleLink.attr("href"))
                m?.run { topicId = m.groupValues.first().toLong() }
                val node = item.getElementsByClass("node").text()
                val topicInfo = item.getElementsByClass("topic_info").select("span").text().split(" • ")
                val commentCount = if (item.getElementsByClass("count_livid").select("a[href]").text().isNullOrBlank()) {
                    "0"
                } else {
                    item.getElementsByClass("count_livid").select("a[href]").text()
                }
                dataList.add(MainPageItem(avatar, title, topicId, node, topicInfo, commentCount))
            }
            return dataList
        }

        /**
         * @return a notification list
         */
        fun getNotificationPage(response: String): Page<Notification> {
            val dataList = ArrayList<Notification>()
            val doc = Jsoup.parse(response)
            val main = doc.getElementById("Main")
            val notifications = main.getElementsByClass("cell").select("div[id]")
            notifications.forEach {
                val tds = it.getElementsByTag("td")
                val avatarUrl = tds[0].getElementsByTag("img")[0].attr("src")
                val userName = tds[1].getElementsByTag("strong")[0].text()
                val titleSpan = tds[1].getElementsByTag("span")[0]
                val title = titleSpan.text()
                val notificationInfo = titleSpan.select("a[href]")[1].attr("href")
                val topicId = notificationInfo.let { n->
                    val regex = Regex("/t/[0-9]+")
                    val m = regex.find(n)
                    m?.groupValues?.first()?.split("/t/")?.getOrNull(1)?.toLong()
                }?: -1L
                val replyNo = notificationInfo.let { n->
                    val regex = Regex("#reply[0-9]+")
                    val m = regex.find(n)
                    m?.groupValues?.first()?.split("#reply")?.getOrNull(1)?.toLong()
                }?: -1L
                val time = tds[1].getElementsByTag("span")[1].text()
                val payload = tds[1].getElementsByClass("payload").let { p ->
                    if (p.isEmpty()) {
                        ""
                    } else {
                        p[0].text()
                    }
                }
                val isReplay = payload.contains("收藏")
                dataList.add(Notification(avatarUrl, userName, title, topicId, replyNo,time, payload, isReplay))
            }
            val pageInfo = main.getElementsByClass("cell").first().getElementsByTag("input").first()
            val current = pageInfo.attr("value")
            val min = pageInfo.attr("min")
            val max = pageInfo.attr("max")
            return Page(current.toInt(), min.toInt(), max.toInt(), dataList)
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

        /**
         * @return [userName, logoutOnce]
         */
        fun getTopUserInfo(response: String): List<String> {
            val doc = Jsoup.parse(response)
            val top = doc.getElementById("Top")
            val topItem = top.select("a[href]")
            var userName = ""
            var logoutOnce =  ""
            for (i in topItem) {
                val href = i.attr("href")
                if(href.contains("member", true)) {
                    userName = i.text()
                }
                if (href == "#;") {
                    val regex = Regex("once=[0-9]+")
                    val m = regex.find(i.attr("onclick"))
                    m?.let { ms ->
                        ms.groupValues.first().split("=").getOrNull(1)?.let {
                            logoutOnce = it
                        }
                    }
                }
            }
            val userInfo = ArrayList<String>()
            userInfo.add(userName)
            userInfo.add(logoutOnce)
            return userInfo
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

        /**
         * like this
         * 请重新点击一次以领取每日登录奖励 message
         * V2EX › 日常任务
         * 每日登录奖励已领取
         * 已连续登录 58 天
         */
        fun getDailyMissionRedeemResult(response: String): List<String> {
            val doc = Jsoup.parse(response)
            val main = doc.getElementById("Main")
            val list = ArrayList<String>()
//            val message = main.getElementsByClass("message").first().text()
//            list.add(message)
            main.getElementsByClass("cell").forEach {
                list.add(it.text())
            }
            return list
        }

        /**
         * @return return a balance object
         */
        fun getBalance(response: String): Balance {
            val doc = Jsoup.parse(response)
            val money = doc.getElementById("money")
            val balanceArea = money.getElementsByClass("balance_area")
            val balanceImgList = balanceArea.select("img[src]").eachAttr("src")
            val balanceStrings = balanceArea.text().split(" ")
            var gold = "0"
            var silver = "0"
            var bronze = "0"
            when (balanceImgList.size) {
                3 -> {
                    gold = balanceStrings[0]
                    silver = balanceStrings[1]
                    bronze = balanceStrings[2]
                }
                2 -> {
                    if (balanceImgList[0].contains("golden") && balanceImgList[1].contains("silver")) {
                        gold = balanceStrings[0]
                        silver = balanceStrings[1]
                    }
                    if (balanceImgList[0].contains("golden") && balanceImgList[1].contains("bronze")) {
                        gold = balanceStrings[0]
                        bronze = balanceStrings[1]
                    }
                    if (balanceImgList[0].contains("silver") && balanceImgList[1].contains("bronze")) {
                        silver = balanceStrings[0]
                        bronze = balanceStrings[1]
                    }
                }
                1 -> {
                    if (balanceImgList[0].contains("golden")) {
                        gold = balanceStrings.first()
                    }
                    if (balanceImgList[0].contains("silver")) {
                        silver = balanceStrings.first()
                    }
                    if (balanceImgList[0].contains("bronze")) {
                        silver = balanceStrings.first()
                    }
                }
            }
            return Balance(gold, silver, bronze)
        }
    }
}
