package com.airy.v2plus.node

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.repository.NodeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NodeViewModel : ViewModel() {

    val nodes: MutableLiveData<List<Node>> = MutableLiveData()

    fun getAllNode() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = NodeRepository.getInstance().getAllNode()
                withContext(Dispatchers.Main) {
                    nodes.value = result
                }
            } catch (e: Exception) {
                Log.e("NodeViewModel", e.message, e)
            }
        }
    }
}
