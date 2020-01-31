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

data class User(
    val id: Long,
    val username: String?,
    val website: String?,
    val github: String?,
    val psn: String?,
    val btc: String?,
    val bio: String?,
    @SerializedName("tagline")
    val tagLine: String?,
    val twitter: String?,
    val location: String?,
    val created: Long,
    @SerializedName("avatar_mini")
    val avatarMiniUrl: String?,
    @SerializedName("avatar_normal")
    val avatarNormalUrl: String?,
    @SerializedName("avatar_large")
    val avatarLargeUrl: String?,
    val url: String?
) : Serializable, Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readLong(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(username)
        writeString(website)
        writeString(github)
        writeString(psn)
        writeString(btc)
        writeString(bio)
        writeString(tagLine)
        writeString(twitter)
        writeString(location)
        writeLong(created)
        writeString(avatarMiniUrl)
        writeString(avatarNormalUrl)
        writeString(avatarLargeUrl)
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}