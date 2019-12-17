package com.airy.v2plus.bean.official

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by Airy on 2019-08-29
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Reply(
    val id: Long,
    val thanks: Int,
    val content: String?,
    @SerializedName("content_render")
    val contentHtml: String?,
    val publishTime: String?,
    val created: Long,
    @SerializedName("last_modified")
    val last_modified: Long,
    val member: MemberInReply
) : Serializable, Parcelable {
    data class MemberInReply(
        val id: Long,
        @SerializedName("user_name")
        val userName: String?,
        val tagLine: String?,
        @SerializedName("avatar_mini")
        val avatarMiniUrl: String?,
        @SerializedName("avatar_normal")
        val avatarNormalUrl: String?,
        @SerializedName("avatar_large")
        val avatarLargeUrl: String?
    ) : Serializable, Parcelable {
        constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeLong(id)
            writeString(userName)
            writeString(tagLine)
            writeString(avatarMiniUrl)
            writeString(avatarNormalUrl)
            writeString(avatarLargeUrl)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<MemberInReply> =
                object : Parcelable.Creator<MemberInReply> {
                    override fun createFromParcel(source: Parcel): MemberInReply =
                        MemberInReply(source)

                    override fun newArray(size: Int): Array<MemberInReply?> = arrayOfNulls(size)
                }
        }
    }

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readLong(),
        source.readSerializable() as MemberInReply
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeInt(thanks)
        writeString(content)
        writeString(contentHtml)
        writeString(publishTime)
        writeLong(created)
        writeLong(last_modified)
        writeSerializable(member)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Reply> = object : Parcelable.Creator<Reply> {
            override fun createFromParcel(source: Parcel): Reply = Reply(source)
            override fun newArray(size: Int): Array<Reply?> = arrayOfNulls(size)
        }
    }
}