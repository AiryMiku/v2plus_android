package com.airy.v2plus.ui.node

import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.base.BaseViewModel

class NodesViewModel(val repository: NodeRepository) : BaseViewModel() {

    private val _nodes = repository.fetchNodesPagedListAsLiveData()
    val nodes = _nodes

}
