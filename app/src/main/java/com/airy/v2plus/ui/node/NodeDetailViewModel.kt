package com.airy.v2plus.ui.node

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.airy.v2plus.model.official.Node
import com.airy.v2plus.ui.base.BaseViewModel

/**
 * Created by Airy on 2020/9/22
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class NodeDetailViewModel: BaseViewModel() {

    val node: MutableLiveData<Node> = MutableLiveData()


    fun getNodeDetail() {

    }
//    var node = _node.switchMap {  }
}