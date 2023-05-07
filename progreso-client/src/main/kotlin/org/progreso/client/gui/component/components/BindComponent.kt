package org.progreso.client.gui.component.components

import org.lwjgl.input.Keyboard
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import java.awt.Color

class BindComponent(
    private val setting: BindSetting,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    private var keyListening = false

    override val visible get() = setting.visibility()

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

        val text = if (keyListening) {
            "Listening.."
        } else {
            Keyboard.getKeyName(setting.value)
        }

        drawStringRelatively(
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawStringRelatively(
            text,
            offsets.textOffset + getStringWidth("${setting.name}  "),
            theme
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        keyListening = !keyListening
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClickedOutside(mouseX, mouseY, mouseButton)

        keyListening = false
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        if (keyCode == Keyboard.KEY_ESCAPE) {
            return
        }

        if (keyListening) {
            setting.value = if (keyCode == Keyboard.KEY_DELETE) Keyboard.KEY_NONE else keyCode
            keyListening = false
        }
    }
}