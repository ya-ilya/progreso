package org.progreso.client.util.client

import com.mojang.blaze3d.platform.InputConstants
import org.lwjgl.glfw.GLFW
import java.util.*

object KeyboardUtil {

    val keyMap: Map<Int, String> by lazy {
        GLFW::class.java.fields
            .filter { it.type == Int::class.java }
            .filter { it.name.startsWith("GLFW_KEY_") }
            .associate {
                val key = it.get(null) as Int
                key to getKeyName(key)
            }
    }

    fun getKeyName(keyCode: Int): String {
        return when (keyCode) {
            GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT"
            GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT"
            GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL"
            GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL"
            GLFW.GLFW_KEY_LEFT_ALT -> "LALT"
            GLFW.GLFW_KEY_RIGHT_ALT -> "RALT"
            GLFW.GLFW_KEY_UNKNOWN -> "NONE"
            else -> {
                try {
                    val key = InputConstants.Type.KEYSYM.getOrCreate(keyCode)
                    val name = key.displayName.string

                    if (name.isEmpty()) {
                        key.name.split(".").last().uppercase(Locale.ROOT)
                    } else {
                        name.uppercase(Locale.ROOT)
                    }
                } catch (_: Exception) {
                    "UNKNOWN"
                }
            }
        }
    }

    fun getKeyCode(keyName: String): Int {
        return keyMap.entries
            .firstOrNull { it.value.equals(keyName, ignoreCase = true) }
            ?.key ?: GLFW.GLFW_KEY_UNKNOWN
    }
}