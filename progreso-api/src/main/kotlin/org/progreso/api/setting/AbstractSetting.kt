package org.progreso.api.setting

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Setting abstract class
 *
 * @param name Setting name
 * @param initialValue Setting initial value
 * @param visibility Setting visibility
 */
abstract class AbstractSetting<T : Any>(
    val name: String,
    val initialValue: T,
    val visibility: () -> Boolean = { true }
) {
    protected var valueListeners = mutableSetOf<(T, T) -> Unit>()

    open var value: T by Delegates.observable(initialValue) { _, oldValue, newValue ->
        if (oldValue == newValue) return@observable
        valueListeners.forEach { it.invoke(oldValue, newValue) }
    }

    fun valueChanged(block: (T, T) -> Unit) {
        valueListeners.add(block)
    }

    open fun reset() {
        this.value = initialValue
    }

    @Suppress("UNCHECKED_CAST")
    open fun setAnyValue(any: Any) {
        this.value = any as T
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}