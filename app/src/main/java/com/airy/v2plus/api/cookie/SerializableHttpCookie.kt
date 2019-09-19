package com.airy.v2plus.api.cookie

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.HttpCookie


/**
 * Created by Airy on 2019-09-16
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class SerializableHttpCookie(@Transient private val mCookie: HttpCookie): Serializable {
    private val SERIAL_VERSION_UID = 6374381323722046732L

    @Transient
    private var mClientCookie: HttpCookie? = null

    fun  getmCookie(): HttpCookie {
        var bestCookie: HttpCookie = mCookie
        if (mClientCookie != null) {
            bestCookie = mClientCookie as HttpCookie
        }
        return bestCookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(mCookie.name)
        out.writeObject(mCookie.value)
        out.writeObject(mCookie.comment)
        out.writeObject(mCookie.commentURL)
        out.writeObject(mCookie.domain)
        out.writeLong(mCookie.maxAge)
        out.writeObject(mCookie.path)
        out.writeObject(mCookie.portlist)
        out.writeInt(mCookie.version)
        out.writeBoolean(mCookie.secure)
        out.writeBoolean(mCookie.discard)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        val name = inputStream.readObject() as String
        val value = inputStream.readObject() as String
        mClientCookie = HttpCookie(name, value)
        mClientCookie!!.comment = inputStream.readObject() as String
        mClientCookie!!.commentURL = inputStream.readObject() as String
        mClientCookie!!.domain = inputStream.readObject() as String
        mClientCookie!!.maxAge = inputStream.readLong()
        mClientCookie!!.path = inputStream.readObject() as String
        mClientCookie!!.portlist = inputStream.readObject() as String
        mClientCookie!!.version = inputStream.readInt()
        mClientCookie!!.secure = inputStream.readBoolean()
        mClientCookie!!.discard = inputStream.readBoolean()
    }
}