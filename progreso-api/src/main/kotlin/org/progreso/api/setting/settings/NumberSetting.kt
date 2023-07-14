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
    fun setNumberValue(number: Number) {
        value = when (initialValue) {
            is Short -> number.toShort() as T
            is Int -> number.toInt() as T
            is Float -> number.toFloat() as T
            is Double -> number.toDouble() as T
            else -> throw IllegalArgumentException()
        }
    }
}