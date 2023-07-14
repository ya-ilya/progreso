package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.SettingComponent
import org.progreso.client.gui.invoke
import java.awt.Color
import kotlin.math.floor

class SliderComponent(
    setting: NumberSetting<*>,
    height: Int,
    parent: AbstractComponent
) : SettingComponent<NumberSetting<*>>(setting, height, parent) {
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

    init {
        sliderWidth =
            floor(sliderMaxWidth * (setting.value.toDouble() / (setting.max.toDouble() - setting.min.toDouble()))).toInt()

        if (sliderWidth > sliderMaxWidth) {
            sliderWidth = sliderMaxWidth
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        super.render(context, mouseX, mouseY)

        if (dragging) {
            sliderWidth = if (mouseX < sliderStartX) {
                0
            } else if (mouseX > sliderEndX) {
                sliderMaxWidth
            } else {
                mouseX - x - SLIDER_START_OFFSET
            }

            try {
                setting.setNumberValue(
                    when {
                        setting.value == setting.max.toDouble() - 0.1f && sliderWidth == width - 1 -> {
                            setting.max
                        }

                        else -> {
                            String.format(
                                "%.1f",
                                setting.min.toDouble() + (setting.max.toDouble() - setting.min.toDouble()) * (sliderWidth.toFloat() / sliderMaxWidth)
                            ).replace(",", ".").toDouble()
                        }
                    }
                )
            } catch (ex: NumberFormatException) {
                // Ignored
            }
        }

        context {
            drawTextRelatively(
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawTextRelatively(
                setting.value.toString(),
                width - 10 - getTextWidth(setting.value.toString()),
                Color.WHITE
            )
            drawRect(
                sliderStartX,
                sliderStartY,
                sliderWidth,
                SLIDER_HEIGHT,
                mainColor
            )
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)

        if (mouseX in sliderStartX..sliderEndX && mouseY in sliderStartY..sliderEndY) {
            dragging = true
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        dragging = false
    }
}