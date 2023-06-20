package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.InputUtil
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.SettingComponent
import org.progreso.client.gui.invoke
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.util.client.KeyboardUtil
import java.awt.Color

class BindComponent(
    setting: BindSetting,
    height: Int,
    parent: AbstractComponent
) : SettingComponent<BindSetting>(setting, height, parent) {
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