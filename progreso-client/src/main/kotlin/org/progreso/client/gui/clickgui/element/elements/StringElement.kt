package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import org.progreso.api.setting.settings.StringSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.getTextWidth
import org.progreso.client.gui.invoke
import java.awt.Color

class StringElement(
    setting: StringSetting,
    height: Int,
    parent: ParentElement
) : SettingElement<StringSetting>(setting, height, parent) {
    private var stringEditing = false
    private var stringEditor = ""

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
        drawTextRelatively(
            this@StringElement,
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawTextRelatively(
            this@StringElement,
            if (stringEditing) stringEditor else setting.value,
            offsets.textOffset + getTextWidth("${setting.name}  "),
            if (stringEditing) Color.WHITE else mainColor
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        stringEditing = !stringEditing
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        stringEditing = false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int) {
        when (keyCode) {
            InputUtil.GLFW_KEY_ESCAPE -> {
                stringEditing = false
            }

            InputUtil.GLFW_KEY_ENTER, InputUtil.GLFW_KEY_KP_ENTER -> {
                setting.value = stringEditor
                stringEditor = ""
                stringEditing = false
            }

            InputUtil.GLFW_KEY_BACKSPACE -> {
                stringEditor = stringEditor.dropLast(1)
            }
        }
    }

    override fun charTyped(char: Char) {
        stringEditor += char
    }
}