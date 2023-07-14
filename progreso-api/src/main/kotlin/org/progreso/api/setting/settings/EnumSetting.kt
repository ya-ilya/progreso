package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

class EnumSetting<T : Enum<T>>(
    name: String,
    initialValue: T,
    visibility: () -> Boolean = { true }
) : AbstractSetting<T>(name, initialValue, visibility) {
    private val entries: Array<T> = value.javaClass.enumConstants

    fun next() {
        value = entries.getOrElse(entries.indexOf(value) + 1) { entries[0] }
    }

    fun prev() {
        value = entries.getOrElse(entries.indexOf(value) - 1) { entries.last() }
    }

    @Suppress("UNCHECKED_CAST")
    override fun setAnyValue(any: Any) {
        if (initialValue.javaClass.isInstance(any)) {
            value = any as T
            return
        }

        value = entries.first { it.name == any.toString() }
    }
}