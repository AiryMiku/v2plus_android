package com.airy.v2plus.ui.node

import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.base.BaseViewModel

class NodeViewModel(val repository: NodeRepository) : BaseViewModel() {

    val nodes = repository.fetchNodesPagedListAsLiveData()

}
