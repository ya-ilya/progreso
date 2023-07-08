package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import org.progreso.api.setting.settings.StringSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.SettingComponent
import org.progreso.client.gui.invoke
import java.awt.Color

class StringComponent(
    setting: StringSetting,
    height: Int,
    parent: AbstractComponent
) : SettingComponent<StringSetting>(setting, height, parent) {
    private var stringEditing = false
    private var stringEditor = StringEditor()

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        context {
            drawTextRelatively(
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawTextRelatively(
                if (stringEditing) stringEditor.string else setting.value,
                offsets.textOffset + getTextWidth("${setting.name}  "),
                if (stringEditing) Color.WHITE else theme
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        stringEditing = !stringEditing
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClickedOutside(mouseX, mouseY, mouseButton)

        stringEditing = false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int) {
        super.keyPressed(keyCode, scanCode)

        when (keyCode) {
            InputUtil.GLFW_KEY_ESCAPE -> {
                stringEditing = false
            }

            InputUtil.GLFW_KEY_ENTER, InputUtil.GLFW_KEY_KP_ENTER -> {
                setting.value = stringEditor.string
                stringEditor = StringEditor()
                stringEditing = false
            }

            InputUtil.GLFW_KEY_BACKSPACE -> {
                stringEditor = StringEditor(stringEditor.string.dropLast(1))
            }
        }
    }

    override fun charTyped(char: Char) {
        super.charTyped(char)

        stringEditor = StringEditor(stringEditor.string + char)
    }

    private data class StringEditor(val string: String = "")
}