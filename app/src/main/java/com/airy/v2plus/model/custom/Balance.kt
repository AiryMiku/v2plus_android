package com.airy.v2plus.model.custom

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by Airy on 2019-12-05
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class Balance(
    val gold: String? = "0",
    val silver: String? = "0",
    val bronze: String? = "0")
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(gold)
        parcel.writeString(silver)
        parcel.writeString(bronze)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Balance> {
        override fun createFromParcel(parcel: Parcel): Balance {
            return Balance(parcel)
        }

        override fun newArray(size: Int): Array<Balance?> {
            return arrayOfNulls(size)
        }
    }
}