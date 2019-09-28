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

class SerializableHttpCookie(@Transient private val cookie: HttpCookie): Serializable {

    companion object {
        private const val serialVersionUID = 6374381323722046732L
    }

    @Transient
    private var mClientCookie: HttpCookie? = null

    fun  getCookies(): HttpCookie {
        var bestCookie: HttpCookie = cookie
        if (mClientCookie != null) {
            bestCookie = mClientCookie as HttpCookie
        }
        return bestCookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie.name)
        out.writeObject(cookie.value)
        out.writeObject(cookie.comment)
        out.writeObject(cookie.commentURL)
        out.writeObject(cookie.domain)
        out.writeLong(cookie.maxAge)
        out.writeObject(cookie.path)
        out.writeObject(cookie.portlist)
        out.writeInt(cookie.version)
        out.writeBoolean(cookie.secure)
        out.writeBoolean(cookie.discard)
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

    override fun toString(): String {
        return mClientCookie.toString()
    }
}