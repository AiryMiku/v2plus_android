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

data class Node(
    val id: Long,
    val topics: Long,
    val title: String,
    val name: String,
    val header: String,
    @SerializedName("parent_node_name")
    val parentNodeName: String,
    @SerializedName("title_alternative")
    val titleAlternative: String,
    @SerializedName("avatar_mini")
    val avatarMiniUrl: String,
    @SerializedName("avatar_normal")
    val avatarNormalUrl: String,
    @SerializedName("avatar_large")
    val avatarLargeUrl: String,
    val starts: Long,
    val root: Boolean,
    val url: String,
    val footer: String
) : Serializable, Parcelable {
    constructor(source: Parcel) : this(
    source.readLong(),
    source.readLong(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readString(),
    source.readLong(),
    1 == source.readInt(),
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(topics)
        writeString(title)
        writeString(name)
        writeString(header)
        writeString(parentNodeName)
        writeString(titleAlternative)
        writeString(avatarMiniUrl)
        writeString(avatarNormalUrl)
        writeString(avatarLargeUrl)
        writeLong(starts)
        writeInt((if (root) 1 else 0))
        writeString(url)
        writeString(footer)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Node> = object : Parcelable.Creator<Node> {
            override fun createFromParcel(source: Parcel): Node = Node(source)
            override fun newArray(size: Int): Array<Node?> = arrayOfNulls(size)
        }
    }
}