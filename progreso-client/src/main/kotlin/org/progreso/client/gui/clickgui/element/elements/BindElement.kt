package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.getTextWidth
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
        val text = if (keyListening) {
            "Listening.."
        } else {
            when (setting.value) {
                -1, 0, 256 -> "NONE"
                else -> KeyboardUtil.getKeyName(setting.value, -1)
            }
        }

        context {
            drawTextRelatively(
                this@BindElement,
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawTextRelatively(
                this@BindElement,
                text,
                offsets.textOffset + getTextWidth("${setting.name}  "),
                mainColor
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        keyListening = !keyListening
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        keyListening = false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            return
        }

        if (keyListening) {
            setting.value = if (keyCode == InputUtil.GLFW_KEY_DELETE) -1 else keyCode
            keyListening = false
        }
    }
}