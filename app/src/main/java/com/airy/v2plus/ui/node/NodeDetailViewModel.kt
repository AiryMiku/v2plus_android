package com.airy.v2plus.ui.node

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.model.official.Node
import com.airy.v2plus.model.official.Topic
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.base.BaseViewModel
import kotlinx.coroutines.channels.Channel

/**
 * Created by Airy on 2020/9/22
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class NodeDetailViewModel(val repository: NodeRepository): BaseViewModel() {

    val node: MutableLiveData<Node> = MutableLiveData()

    val topics: MutableLiveData<List<Topic>> = MutableLiveData()


    fun getNodeDetail(name: String) {
        launchOnIO({
            val n = repository.getNodeDetailByName(name)
            node.postValue(n)
        })
    }

    fun getTopicsByNodeName(name: String) {
        launchOnIO({
            val ts = V2exRetrofitService.getV2exApi().getTopicsByNodeName(name)
            topics.postValue(ts)
        })
    }
//    var node = _node.switchMap {  }
}