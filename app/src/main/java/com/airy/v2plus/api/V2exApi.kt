package com.airy.v2plus.api

import com.airy.v2plus.bean.official.Node
import com.airy.v2plus.bean.official.Reply
import com.airy.v2plus.bean.official.Topic
import com.airy.v2plus.bean.official.User
import com.sun.org.apache.xalan.internal.lib.NodeInfo
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
    fun getTopicHot(): List<Topic>

    @GET("topics/latest.json")     //每天最新
    fun getTopicLatest(): List<Topic>

    @GET("nodes/show.json")     //节点详情
    fun getNodeDetial(@Query("name") name: String): Node

    @GET("nodes/all.json")     //获取所有节点
    fun getAllNode(): List<Node>

    @GET("topics/show.json")       //根据节点名获取所有帖子
    fun getTopicsByNodeName(@Query("node_name") node_name: String): List<Topic>

    @GET("replies/show.json")      //帖子回复
    fun getReplise(@Query("topic_id") topic_id: Int): List<Reply>

    @GET("members/show.json")     //获取用户详情
    fun getUserInfo(@Query("username") username: String): User

    @GET("topics/show.json")   //用户发的帖子
    fun getTopicsByUserName(@Query("username") username: String): List<Topic>

}