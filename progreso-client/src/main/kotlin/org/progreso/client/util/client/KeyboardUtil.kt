package org.progreso.client.util.client

import net.minecraft.client.util.InputUtil

object KeyboardUtil {
    fun getKeyName(keyCode: Int, scanCode: Int = -1): String {
        return InputUtil.fromKeyCode(keyCode, scanCode)
            .toString()
            .split(".")
            .last()
            .uppercase()
    }
}