package com.airy.v2plus.network.api

import com.airy.v2plus.model.official.Node
import com.airy.v2plus.model.official.Reply
import com.airy.v2plus.model.official.Topic
import com.airy.v2plus.model.official.User
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Airy on 2019-07-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 *
 * web html
 */

@JvmSuppressWildcards
interface V2exApi {

    @GET("topics/hot.json")     // 每天热门
    suspend fun getTopicHot(): List<Topic>

    @GET("topics/latest.json")     // 每天最新
    suspend fun getTopicLatest(): List<Topic>

    @GET("nodes/show.json")     // 节点详情
    suspend fun getNodeDetail(@Query("name") name: String): Node

    @GET("members/show.json")     // 获取用户详情
    suspend fun getUserById(@Query("id") id: Long): User

    @GET("members/show.json")     // 获取用户详情
    suspend fun getUserByName(@Query("username") username: String): User

    // somebody found hide api
    @GET("nodes/all.json")     // 获取所有节点,真的多
    suspend fun getAllNode(): List<Node>

    @GET("topics/show.json")       // 根据节点名获取所有帖子,才只有最新10条
    suspend fun getTopicsByNodeName(@Query("node_name") node_name: String): List<Topic>

    @GET("replies/show.json")      // 帖子回复,所有的
    suspend fun getRepliesByTopicId(@Query("topic_id") topicId: Long): List<Reply>

    @GET("topics/show.json")   // 用户发的帖子
    suspend fun getTopicsByUserName(@Query("username") username: String): List<Topic>

    // i found one
    @GET("topics/show.json")   // 指定id的帖子
    suspend fun getTopicById(@Query("id") id: Long): List<Topic>    // 注意这里只有一个,返回的json是特么的列表

}