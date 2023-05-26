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
    open var value: T by Delegates.observable(initialValue) { _, oldValue, newValue ->
        valueChanged(oldValue, newValue)
    }

    open fun valueChanged(oldValue: T, newValue: T) {}

    @Suppress("UNCHECKED_CAST")
    open fun setAnyValue(any: Any) {
        this.value = any as T
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}