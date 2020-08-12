package com.airy.v2plus.repository

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.airy.v2plus.App
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.db.NodeDao
import com.airy.v2plus.db.V2plusDb
import com.airy.v2plus.launchOnIOInGlobal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


/**
 * Created by Airy on 2019-11-07
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class NodeRepository {

    private val TAG = "NodeRepository"

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

    suspend fun fetchAllNodesFlow(updateFromNetwork: Boolean = false): Flow<List<Node>> {
        return flow {
            try {
                var nodes = nodeDao.getAllNodesList()
                if (nodes.isEmpty() || updateFromNetwork) {
                    nodes = V2exRetrofitService.getV2exApi().getAllNode()
                    nodeDao.insert(nodes)
                }
                emit(nodes)
            } catch (e: Exception) {
                Log.e(TAG, e.message, e)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun fetchNodesByName(value: String): List<Node> {
        val nodes = nodeDao.getNodesListByName(value)
        return nodes
    }

    @MainThread
    fun fetchAllNodesPagedListLiveData(updateFromNetwork: Boolean = false): LiveData<PagedList<Node>> {
        if (updateFromNetwork) {
            launchOnIOInGlobal({
                nodeDao.insert(V2exRetrofitService.getV2exApi().getAllNode())
            })
        }
        val callback = NodeBoundaryCallback(nodeDao)
        return nodeDao.getAllNodesDataSource().toLiveData(
            Config(
                pageSize = 64,
                prefetchDistance = 4,
                enablePlaceholders = true
            ),
            boundaryCallback = callback
        )
    }
}

class NodeBoundaryCallback(private val dao: NodeDao): PagedList.BoundaryCallback<Node>() {
    override fun onZeroItemsLoaded() {
        Log.d(this::class.simpleName, "onZeroItemsLoaded")
        launchOnIOInGlobal({
            val nodes = V2exRetrofitService.getV2exApi().getAllNode()
            dao.insert(nodes)
        })
    }
}