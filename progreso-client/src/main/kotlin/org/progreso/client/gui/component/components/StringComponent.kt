package org.progreso.client.gui.component.components

import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.input.Keyboard
import org.progreso.api.setting.settings.StringSetting
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.manager.managers.render.TextRenderManager
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import java.awt.Color

class StringComponent(
    private val setting: StringSetting,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    private var stringEditing = false
    private var stringEditor = StringEditor()

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

        drawStringRelatively(
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawStringRelatively(
            if (stringEditing) stringEditor.string else setting.value,
            offsets.textOffset + TextRenderManager.getStringWidth("${setting.name}  "),
            if (stringEditing) Color.WHITE else theme
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        stringEditing = !stringEditing
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClickedOutside(mouseX, mouseY, mouseButton)

        stringEditing = false
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                stringEditing = false
            }

            Keyboard.KEY_RETURN -> {
                setting.value = stringEditor.string
                stringEditor = StringEditor()
                stringEditing = false
            }

            Keyboard.KEY_BACK -> {
                stringEditor = StringEditor(stringEditor.string.dropLast(1))
            }

            else -> {
                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    stringEditor = StringEditor(stringEditor.string + typedChar)
                }
            }
        }
    }

    private data class StringEditor(val string: String = "")
}