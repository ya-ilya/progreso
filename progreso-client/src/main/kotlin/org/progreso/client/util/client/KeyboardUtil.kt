package org.progreso.client.util.client

import net.minecraft.client.util.InputUtil

object KeyboardUtil {
    var keyMap = InputUtil::class.java
        .fields.filter { it.type == Int::class.java }
        .filter { it.name.startsWith("GLFW_KEY_") }
        .associate {
            val key = it.get(null) as Int
            key to getKeyName(key)
        }

    fun getKeyName(keyCode: Int, scanCode: Int = -1): String {
        return when (keyCode) {
            InputUtil.GLFW_KEY_LEFT_SHIFT -> "LSHIFT"
            InputUtil.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT"
            InputUtil.GLFW_KEY_LEFT_CONTROL -> "LCTRL"
            InputUtil.GLFW_KEY_RIGHT_CONTROL -> "RCTRL"
            InputUtil.GLFW_KEY_LEFT_ALT -> "LALT"
            InputUtil.GLFW_KEY_RIGHT_ALT -> "RALT"
            else -> InputUtil.fromKeyCode(keyCode, scanCode)
                .toString()
                .split(".")
                .last()
                .uppercase()
        }
    }

    fun getKeyCode(keyName: String): Int {
        return keyMap.entries
            .first { it.value.uppercase() == keyName.uppercase() }
            .key
    }
}