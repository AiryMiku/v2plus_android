package com.airy.v2plus.repository

import com.airy.v2plus.network.V2exRetrofitService
import com.airy.v2plus.bean.official.Node


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

    suspend fun getAllNode(): List<Node> {
        return V2exRetrofitService.getV2exApi().getAllNode()
    }
}