package org.progreso.api.common

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MutableLazy<T>(private val block: () -> T) : ReadWriteProperty<Any?, T> {
    companion object {
        fun <T> mutableLazy(block: () -> T): MutableLazy<T> {
            return MutableLazy(block)
        }
    }

    private var value: Any? = UNINITIALIZED

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == UNINITIALIZED) this.value = block()
        return value as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (this.value == UNINITIALIZED) this.value = block()
        this.value = value
    }

    private object UNINITIALIZED
}