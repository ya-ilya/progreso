package org.progreso.client.gui.component.components

import org.progreso.api.setting.settings.BooleanSetting
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.util.Render2DUtil.drawRect
import org.progreso.client.util.Render2DUtil.drawStringRelatively
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

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

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

        if (setting.value) {
            drawRect(
                buttonStartX + BUTTON_WIDTH / 2,
                buttonStartY,
                BUTTON_WIDTH / 2,
                BUTTON_HEIGHT,
                theme
            )
        } else {
            drawRect(
                buttonStartX,
                buttonStartY,
                BUTTON_WIDTH / 2,
                BUTTON_HEIGHT,
                theme
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        if (mouseButton == 0) {
            setting.value = !setting.value
        }
    }
}