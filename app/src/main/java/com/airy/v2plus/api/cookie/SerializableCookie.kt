package com.airy.v2plus.api.cookie

import okhttp3.Cookie
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


/**
 * Created by Airy on 2019-10-11
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class SerializableCookie(@Transient
                         private val cookie: Cookie): Serializable {
    companion object {
        private const val serialVersionUID = 6374381828722046732L
    }

    @Transient
    private var clientCookie: Cookie? = null

    fun getCookie(): Cookie {
        var bestCookie = cookie
        if (clientCookie != null) {
            bestCookie = clientCookie as Cookie
        }
        return bestCookie
    }

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(cookie.name)
        out.writeObject(cookie.value)
        out.writeLong(cookie.expiresAt)
        out.writeObject(cookie.domain)
        out.writeObject(cookie.path)
        out.writeBoolean(cookie.secure)
        out.writeBoolean(cookie.httpOnly)
        out.writeBoolean(cookie.hostOnly)
        out.writeBoolean(cookie.persistent)
    }

    @Throws(IOException::class)
    private fun readObject(oin: ObjectInputStream) {
        val name = oin.readObject() as String
        val value = oin.readObject() as String
        val expiresAt = oin.readLong()
        val domain = oin.readObject() as String
        val path = oin.readObject() as String
        val secure = oin.readBoolean()
        val httpOnly = oin.readBoolean()
        val hostOnly = oin.readBoolean()
        val persistent = oin.readBoolean()

        val builder = Cookie.Builder()
            .name(name)
            .value(value)
            .expiresAt(expiresAt)
            .path(path)

        if (hostOnly) {
            builder.hostOnlyDomain(domain)
        } else {
            builder.domain(domain)
        }
        if (secure) {
            builder.httpOnly()
        }
        if (httpOnly) {
            builder.httpOnly()
        }
        clientCookie = builder.build()
    }
}