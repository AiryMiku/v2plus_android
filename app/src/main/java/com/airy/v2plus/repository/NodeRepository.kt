package com.airy.v2plus.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.model.official.Node
import com.airy.v2plus.db.dao.NodeDao
import com.airy.v2plus.launchOnIOInGlobal
import com.orhanobut.logger.Logger


/**
 * Created by Airy on 2019-11-07
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
private const val TAG = "NodeRepository"

class NodeRepository(private val dao: NodeDao) : Repository {

    suspend fun getNodeDetailByName(name: String): Node {
        return V2exRetrofitService.getV2exApi().getNodeDetail(name)
    }

    @MainThread
    fun fetchNodesPagedListAsLiveData(updateFromNetwork: Boolean = false): LiveData<PagedList<Node>> {
        if (updateFromNetwork) {
            launchOnIOInGlobal({
                this@NodeRepository.dao.insert(V2exRetrofitService.getV2exApi().getAllNode())
            })
        }
        val callback = NodeBoundaryCallback(this.dao)
        return this.dao.getNodesDataSource().toLiveData(
            Config(
                pageSize = 16,
                prefetchDistance = 4,
                enablePlaceholders = true
            ),
            boundaryCallback = callback
        )
    }
}

class NodeBoundaryCallback(private val dao: NodeDao): PagedList.BoundaryCallback<Node>() {
    override fun onZeroItemsLoaded() {
        Logger.d(TAG, "onZeroItemsLoaded")
        launchOnIOInGlobal({
            val nodes = V2exRetrofitService.getV2exApi().getAllNode()
            dao.insert(nodes)
        })
    }
}