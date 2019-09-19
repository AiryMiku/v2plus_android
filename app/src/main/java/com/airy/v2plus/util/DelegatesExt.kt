package com.airy.juju.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by Airy on 2019-07-09
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

object DelegatesExt {

    fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()

}

private class NotNullSingleValueVar<T> : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value?: throw IllegalStateException("not initilaized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value else throw IllegalStateException("alread initialized")
    }
}