package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.util.Render2DUtil.drawRect
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import java.awt.Color
import kotlin.math.floor

class SliderComponent(
    private val setting: NumberSetting<*>,
    height: Int,
    parent: AbstractComponent
) : ChildComponent(height, parent) {
    private companion object {
        const val SLIDER_START_OFFSET = 5
        const val SLIDER_END_OFFSET = 5
        const val SLIDER_HEIGHT = 1
    }

    private var dragging = false
    private var sliderWidth = 0

    private val sliderMaxWidth get() = width - SLIDER_START_OFFSET - SLIDER_END_OFFSET
    private val sliderStartX get() = x + SLIDER_START_OFFSET
    private val sliderEndX get() = x + width - SLIDER_END_OFFSET
    private val sliderStartY get() = y + height - 2 * SLIDER_HEIGHT
    private val sliderEndY get() = sliderStartY + SLIDER_HEIGHT

    override val visible get() = setting.visibility()

    init {
        sliderWidth =
            floor(sliderMaxWidth * (setting.value.toDouble() / (setting.max.toDouble() - setting.min.toDouble()))).toInt()

        if (sliderWidth > sliderMaxWidth) {
            sliderWidth = sliderMaxWidth
        }
    }

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawComponent(mouseX, mouseY, partialTicks)

        if (dragging) {
            sliderWidth = if (mouseX < sliderStartX) {
                0
            } else if (mouseX > sliderEndX) {
                sliderMaxWidth
            } else {
                mouseX - x - SLIDER_START_OFFSET
            }

            setting.setNumberValue(
                String.format(
                    "%.1f",
                    setting.min.toDouble() + (setting.max.toDouble() - setting.min.toDouble()) * (sliderWidth.toFloat() / sliderMaxWidth)
                ).toDouble()
            )

            if (setting.value == setting.max.toDouble() - 0.1f && sliderWidth == width - 1) {
                setting.setNumberValue(setting.max)
            }
        }

        drawStringRelatively(
            setting.name,
            offsets.textOffset,
            Color.WHITE
        )
        drawStringRelatively(
            setting.value.toString(),
            width - 10 - getStringWidth(setting.value.toString()),
            Color.WHITE
        )
        drawRect(
            sliderStartX,
            sliderStartY,
            sliderWidth,
            SLIDER_HEIGHT,
            theme
        )
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        if (mouseX in sliderStartX..sliderEndX && mouseY in sliderStartY..sliderEndY) {
            dragging = true
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        dragging = false
    }
}