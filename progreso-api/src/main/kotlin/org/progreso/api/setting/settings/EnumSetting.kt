package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

open class EnumSetting<T : Enum<T>>(
    name: String,
    initialValue: T,
    visibility: () -> Boolean = { true }
) : AbstractSetting<T>(name, initialValue, visibility) {
    private val values: Array<T> = value.javaClass.enumConstants

    fun next() {
        value = values.getOrElse(values.indexOf(value) + 1) { values[0] }
    }

    fun prev() {
        value = values.getOrElse(values.indexOf(value) - 1) { values.last() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun setAnyValue(any: Any) {
        if (initialValue.javaClass.isInstance(any)) {
            value = any as T
            return
        }

        value = values.first { it.name == any.toString() }
    }
}