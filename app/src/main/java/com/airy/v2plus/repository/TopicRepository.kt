package com.airy.v2plus.repository

import com.airy.v2plus.api.V2exRetrofitService
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.bean.official.Topic


/**
 * Created by Airy on 2019-12-19
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class TopicRepository {

    companion object {
        @Volatile
        private var instance: TopicRepository? = null

        fun getInstance(): TopicRepository = instance ?: synchronized(this) {
            instance ?: TopicRepository().also { instance = it }
        }
    }

    suspend fun getTopic(id: Long): Topic {
        return V2exRetrofitService.getV2exApi().getTopicById(id).first()
    }

    suspend fun getRepliesByTopicId(id: Long): List<Reply> {
        return V2exRetrofitService.getV2exApi().getRepliesByTopicId(id)
    }
}