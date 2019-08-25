package com.airy.v2plus.repository

import com.airy.v2plus.api.V2plusRetrofitService
import com.airy.v2plus.bean.custom.PageCell
import org.jsoup.Jsoup


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class MainPageRepository {

    companion object {
        @Volatile
        private var instance: MainPageRepository? = null

        fun getInstance(): MainPageRepository = instance ?: synchronized(this) {
            instance ?: MainPageRepository().also { instance = it }
        }
    }

    suspend fun getMainPage(): List<PageCell> {
        val dataList = ArrayList<PageCell>()
        val bodyString =  V2plusRetrofitService.getV2plusApi().getMainPage()
        val doc = Jsoup.parse(bodyString)
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
}