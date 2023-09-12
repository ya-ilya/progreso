package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

class NumberSetting<T>(
    name: String,
    initialValue: T,
    private val range: ClosedRange<T>,
    visibility: () -> Boolean = { true }
) : AbstractSetting<T>(name, initialValue, visibility) where T : Number, T : Comparable<T> {
    init {
        if (initialValue is Long) {
            throw IllegalArgumentException("Long isn't supported in number setting")
        }
    }

    val min: T get() = range.start
    val max: T get() = range.endInclusive

    @Suppress("UNCHECKED_CAST")
    override fun setAnyValue(any: Any) {
        if (any !is Number) throw IllegalArgumentException()

        value = when (initialValue) {
            is Short -> any.toShort() as T
            is Int -> any.toInt() as T
            is Float -> any.toFloat() as T
            is Double -> any.toDouble() as T
            else -> throw IllegalArgumentException()
        }
    }
}