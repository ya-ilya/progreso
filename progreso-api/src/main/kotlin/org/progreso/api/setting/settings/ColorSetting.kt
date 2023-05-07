package org.progreso.api.setting.settings

import org.progreso.api.setting.AbstractSetting
import java.awt.Color

class ColorSetting(
    name: String,
    initialValue: Color,
    visibility: () -> Boolean
) : AbstractSetting<Color>(name, initialValue, visibility) {
    override fun setAnyValue(any: Any) {
        if (any is Color) {
            value = any
            return
        }

        val (red, green, blue, alpha) = hexToRGBA(any.toString())
        value = Color(red, green, blue, alpha)
    }

    private companion object {
        fun hexToRGBA(colorHexString: String): Array<Int> {
            var local = colorHexString
            val result = arrayOf(0, 0, 0, 0)
            if (local.startsWith("#")) {
                local = local.substring(1, local.length)
            }
            val len = local.length
            if (len > 8) {
                throw IllegalArgumentException("Bad hex length ($len)")
            }
            val rgbSub: String
            when (len) {
                8 -> {
                    val h = hexCharToInt(local[0])
                    val l = hexCharToInt(local[1])
                    result[3] = h * 16 + l
                    rgbSub = local.substring(2, len)
                }

                7 -> {
                    val h = 0xf
                    val l = hexCharToInt(local[0])
                    result[3] = h * 16 + l
                    rgbSub = local.substring(1, len)
                }

                else -> {
                    result[3] = 255
                    rgbSub = local
                }
            }

            // Red
            run {
                val h = hexCharToInt(rgbSub[0])
                val l = hexCharToInt(rgbSub[1])
                result[0] = h * 16 + l
            }
            // Green
            run {
                val h = hexCharToInt(rgbSub[2])
                val l = hexCharToInt(rgbSub[3])
                result[1] = h * 16 + l
            }
            // Blue
            run {
                val h = hexCharToInt(rgbSub[4])
                val l = hexCharToInt(rgbSub[5])
                result[2] = h * 16 + l
            }
            return result
        }

        fun hexCharToInt(ch: Char): Int {
            return when (ch) {
                in '0'..'9' -> {
                    ch.code - '0'.code
                }

                in 'a'..'z' -> {
                    ch.code - 97 + 10
                }

                in 'A'..'Z' -> {
                    ch.code - 65 + 10
                }

                else -> {
                    throw IllegalArgumentException("Char is not hex")
                }
            }
        }
    }
}