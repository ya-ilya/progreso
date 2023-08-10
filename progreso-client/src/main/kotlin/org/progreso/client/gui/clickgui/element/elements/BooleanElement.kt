package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.BooleanSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.invoke
import java.awt.Color

class BooleanElement(
    setting: BooleanSetting,
    height: Int,
    parent: ParentElement
) : SettingElement<BooleanSetting>(setting, height, parent) {
    private companion object {
        const val BUTTON_WIDTH = 8
        const val BUTTON_HEIGHT = 4
        const val BUTTON_END_OFFSET = 5
    }

    private val buttonStartX get() = x + width - BUTTON_END_OFFSET - BUTTON_WIDTH
    private val buttonStartY get() = y + height.div(2) - BUTTON_HEIGHT.div(2)

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
        drawTextRelatively(
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawRect(
            buttonStartX,
            buttonStartY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Color(80, 80, 80, 120)
        )
        drawCircle(
            buttonStartX + if (setting.value) BUTTON_WIDTH else 0,
            buttonStartY + 2,
            0.0, 360.0,
            40, 3.0,
            mainColor
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0) {
            setting.value = !setting.value
        }
    }
}