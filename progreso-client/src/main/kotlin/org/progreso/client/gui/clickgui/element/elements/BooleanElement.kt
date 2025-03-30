package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.BooleanSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.invoke
import org.progreso.client.util.render.drawCircle
import org.progreso.client.util.render.render2D
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

        val BUTTON_BACK_RECT_COLOR = Color(80, 80, 80, 120)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) = context {
        val buttonStartX = x + width - BUTTON_END_OFFSET - BUTTON_WIDTH
        val buttonStartY = y + height.div(2) - BUTTON_HEIGHT.div(2)

        drawTextRelatively(
            this@BooleanElement,
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawRect(
            buttonStartX,
            buttonStartY,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            BUTTON_BACK_RECT_COLOR
        )

        render2D(context) {
            drawCircle(
                buttonStartX + if (setting.value) BUTTON_WIDTH else 0,
                buttonStartY + 2,
                0.0, 360.0,
                40, 3.0,
                mainColor
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (button == 0) {
            setting.value = !setting.value
        }
    }
}