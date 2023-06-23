package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting

class NumberSetting<T>(
    name: String,
    initialValue: T,
    private val range: ClosedRange<T>,
    visibility: () -> Boolean = { true }
) : AbstractSetting<T>(name, initialValue, visibility) where T : Number, T : Comparable<T> {
    val min: T get() = range.start
    val max: T get() = range.endInclusive

    @Suppress("UNCHECKED_CAST")
    fun setNumberValue(number: Number) {
        when (initialValue) {
            is Int -> {
                value = number.toInt() as T
            }

            is Float -> {
                value = number.toFloat() as T
            }

            is Double -> {
                value = number.toDouble() as T
            }
        }
    }
}