package com.airy.v2plus.ui.node

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.base.BaseViewModel

class NodeViewModel : BaseViewModel() {

    val repository by lazy { NodeRepository.getInstance()  }

    val nodes: MutableLiveData<List<Node>> = MutableLiveData()

    init {
        getAllNode()
    }

    fun getAllNode() {
        launchOnIO({
            val result = repository.fetchAllNode()
            nodes.postValue(result)

//            val r = repository.fetchAllNodesFlow().asLiveData()
//            nodes = r
        })

    }

    fun getNodesByName(value: String) {
        launchOnIO({
            val fuzzyValue = "%$value%"
            val result = repository.fetchNodesByName(fuzzyValue)
            nodes.postValue(result)
        })
    }
}
