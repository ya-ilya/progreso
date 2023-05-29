package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.setting.settings.BooleanSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.util.render.RenderContext
import java.awt.Color

class BooleanComponent(
    private val setting: BooleanSetting,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    private companion object {
        const val BUTTON_WIDTH = 8
        const val BUTTON_HEIGHT = 4
        const val BUTTON_END_OFFSET = 5
    }

    private val buttonStartX get() = x + width - BUTTON_END_OFFSET - BUTTON_WIDTH
    private val buttonStartY get() = y + height.div(2) - BUTTON_HEIGHT.div(2)

    override val visible get() = setting.visibility()

    override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        context {
            drawStringRelatively(
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
            drawRect(
                buttonStartX + if (setting.value) BUTTON_WIDTH / 2 else 0,
                buttonStartY,
                BUTTON_WIDTH / 2,
                BUTTON_HEIGHT,
                theme
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        if (button == 0) {
            setting.value = !setting.value
        }
    }
}