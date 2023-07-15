package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.invoke
import org.progreso.client.util.client.KeyboardUtil
import java.awt.Color

class BindElement(
    setting: BindSetting,
    height: Int,
    parent: ParentElement
) : SettingElement<BindSetting>(setting, height, parent) {
    private var keyListening = false

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        val text = if (keyListening) {
            "Listening.."
        } else {
            when (setting.value) {
                InputUtil.GLFW_KEY_LEFT_SHIFT -> "LSHIFT"
                InputUtil.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT"
                InputUtil.GLFW_KEY_LEFT_CONTROL -> "LCTRL"
                InputUtil.GLFW_KEY_RIGHT_CONTROL -> "RCTRL"
                InputUtil.GLFW_KEY_LEFT_ALT -> "LALT"
                InputUtil.GLFW_KEY_RIGHT_ALT -> "RALT"
                -1, 0, 256 -> "NONE"
                else -> KeyboardUtil.getKeyName(setting.value, -1)
            }
        }

        context {
            drawTextRelatively(
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawTextRelatively(
                text,
                offsets.textOffset + getTextWidth("${setting.name}  "),
                mainColor
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        keyListening = !keyListening
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClickedOutside(mouseX, mouseY, mouseButton)

        keyListening = false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int) {
        super.keyPressed(keyCode, scanCode)

        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            return
        }

        if (keyListening) {
            setting.value = if (keyCode == InputUtil.GLFW_KEY_DELETE) -1 else keyCode
            keyListening = false
        }
    }
}