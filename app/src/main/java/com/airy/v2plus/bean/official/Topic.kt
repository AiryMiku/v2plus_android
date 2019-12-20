package com.airy.v2plus.bean.official

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by Airy on 2019-08-25
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Topic(
    val id: Long,
    val title: String?,
    val content: String?,
    @SerializedName("content_rendered")
    val contentHtml: String?,
    val created: Long,
    val url: String?,
    @SerializedName("last_touched")
    val lastTouchedMemberId: Long,
    @SerializedName("last_reply_by")
    val lastReplyMemberName: String?,
    @SerializedName("last_modified")
    val lastModifiedMemberId: Long,
    val replies: Long,
    val node: Node,
    val member: User
) : Serializable, Parcelable {
        constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readString(),
        source.readLong(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readSerializable() as Node,
        source.readSerializable() as User
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeString(content)
        writeString(contentHtml)
        writeLong(created)
        writeString(url)
        writeLong(lastTouchedMemberId)
        writeString(lastReplyMemberName)
        writeLong(lastModifiedMemberId)
        writeLong(replies)
        writeSerializable(node)
        writeSerializable(member)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Topic> = object : Parcelable.Creator<Topic> {
            override fun createFromParcel(source: Parcel): Topic = Topic(source)
            override fun newArray(size: Int): Array<Topic?> = arrayOfNulls(size)
        }
    }
}