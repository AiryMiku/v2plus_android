package com.airy.v2plus.db

import androidx.room.*
import com.airy.v2plus.bean.official.Node

/**
 * Created by Airy on 2020/7/23
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

@Dao
interface NodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nodes: List<Node>)

    @Query("DELETE from nodes")
    suspend fun deleteAll()

    @Query("SELECT * from nodes")
    suspend fun getAllNodesList(): List<Node>

    @Query("SELECT * from nodes WHERE name like :value")
    suspend fun getNodesListByName(value: String): List<Node>
}