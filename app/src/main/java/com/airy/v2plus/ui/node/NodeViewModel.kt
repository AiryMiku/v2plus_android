package com.airy.v2plus.ui.node

import androidx.lifecycle.MutableLiveData
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.base.BaseViewModel

class NodeViewModel : BaseViewModel() {

    val nodes: MutableLiveData<List<Node>> = MutableLiveData()

    fun getAllNode() {
        launchOnIO({
            val result = NodeRepository.getInstance().getAllNode()
            nodes.postValue(result)
        })
    }
}
