package com.airy.v2plus.ui.node

import com.airy.v2plus.repository.NodeRepository
import com.airy.v2plus.ui.base.BaseViewModel

class NodeViewModel : BaseViewModel() {

    val repository by lazy { NodeRepository.getInstance()  }

    val nodes = repository.fetchAllNodesPagedListLiveData()

}
