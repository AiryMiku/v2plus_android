package com.airy.v2plus.repository

import com.airy.v2plus.App
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.db.V2plusDb


/**
 * Created by Airy on 2019-11-07
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NodeRepository {

    companion object {
        @Volatile
        private var instance: NodeRepository? = null

        fun getInstance(): NodeRepository = instance ?: synchronized(this) {
            instance ?: NodeRepository().also { instance = it }
        }
    }

    private val nodeDao by lazy { V2plusDb.getDb(App.getAppContext()).nodeDao() }

    suspend fun fetchAllNode(updateFromNetwork: Boolean = false): List<Node> {
        var nodes = nodeDao.getAllNodesList()
        if (nodes.isEmpty() || updateFromNetwork) {
            nodes = V2exRetrofitService.getV2exApi().getAllNode()
            nodeDao.insert(nodes)
        }
        return nodes
    }

    suspend fun fetchNodesByName(value: String): List<Node> {
        val nodes = nodeDao.getNodesListByName(value)
        return nodes
    }
}