package com.airy.v2plus.ui.topic

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.repository.TopicRepository
import com.airy.v2plus.base.BaseViewModel


/**
 * Created by Airy on 2019-12-19
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class TopicDetailViewModel: BaseViewModel() {

    private val topicRepository by lazy { TopicRepository.getInstance() }

    val topicDetails: MutableLiveData<List<Any>> = MutableLiveData()

    fun getTopicDetails(id: Long) {
        launchOnIO({
            val items = MutableList<Any>(0) {}
            val t = topicRepository.getTopic(id)
            val rs = topicRepository.getRepliesByTopicId(id)
            items.add(t)
            items.addAll(rs)
            topicDetails.postValue(items)
        })
    }

}