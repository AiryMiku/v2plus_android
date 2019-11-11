package com.airy.v2plus.api

import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.bean.official.Topic
import com.airy.v2plus.bean.official.User
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

    @GET("topics/hot.json")     //每天热门
    suspend fun getTopicHot(): List<Topic>

    @GET("topics/latest.json")     //每天最新
    suspend fun getTopicLatest(): List<Topic>

    @GET("nodes/show.json")     //节点详情
    suspend fun getNodeDetial(@Query("name") name: String): Node

    @GET("nodes/all.json")     //获取所有节点
    suspend fun getAllNode(): List<Node>

    @GET("topics/show.json")       //根据节点名获取所有帖子
    suspend fun getTopicsByNodeName(@Query("node_name") node_name: String): List<Topic>

    @GET("replies/show.json")      //帖子回复
    suspend fun getReplise(@Query("topic_id") topic_id: Int): List<Reply>

    @GET("members/show.json")     //获取用户详情
    suspend fun getUserInfo(@Query("username") username: String): User

    @GET("topics/show.json")   //用户发的帖子
    suspend fun getTopicsByUserName(@Query("username") username: String): List<Topic>

}