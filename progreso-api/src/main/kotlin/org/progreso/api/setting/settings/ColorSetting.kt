package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting
import java.awt.Color

open class ColorSetting(
    name: String,
    initialValue: Color,
    visibility: () -> Boolean = { true }
) : AbstractSetting<Color>(name, initialValue, visibility) {
    override fun setAnyValue(any: Any) {
        value = when (any) {
            is Color -> any
            is String -> stringToColor(any)
            else -> throw IllegalArgumentException()
        }
    }

    private companion object {
        fun stringToColor(string: String): Color {
            val array = string.split("-").map { it.toInt() }
            return when (array.size) {
                3 -> Color(array[0], array[1], array[2])
                4 -> Color(array[0], array[1], array[2], array[3])
                else -> throw IllegalArgumentException()
            }
        }
    }
}