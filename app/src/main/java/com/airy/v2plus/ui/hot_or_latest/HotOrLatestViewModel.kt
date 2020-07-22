package com.airy.v2plus.ui.hot_or_latest

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.bean.official.Topic
import com.airy.v2plus.ui.base.BaseViewModel

class HotOrLatestViewModel: BaseViewModel() {

    val latest = MutableLiveData<List<Topic>>()
    val hot = MutableLiveData<List<Topic>>()

    init {
//        when(type) {
//            "latest" -> getLatest()
//            "hot" -> getHot()
//        }
    }

    fun getHot() {
        launchOnIO({
            val r = V2exRetrofitService.getV2exApi().getTopicHot()
            hot.postValue(r)
        })
    }

    fun getLatest() {
        launchOnIO({
            val r = V2exRetrofitService.getV2exApi().getTopicLatest()
            latest.postValue(r)
        })
    }

}