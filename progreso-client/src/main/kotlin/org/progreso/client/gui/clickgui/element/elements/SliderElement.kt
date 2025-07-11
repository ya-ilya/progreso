package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.getTextWidth
import org.progreso.client.gui.invoke
import org.progreso.client.util.render.drawEllipse
import org.progreso.client.util.render.render2D
import java.awt.Color
import kotlin.math.floor

class SliderElement(
    setting: NumberSetting<*>,
    height: Int,
    parent: ParentElement
) : SettingElement<NumberSetting<*>>(setting, height, parent) {
    private companion object {
        const val SLIDER_START_OFFSET = 5
        const val SLIDER_END_OFFSET = 5
        const val SLIDER_HEIGHT = 2
    }

    private var dragging = false
    private var sliderWidth = 0

    private val sliderMaxWidth get() = width - SLIDER_START_OFFSET - SLIDER_END_OFFSET
    private val sliderStartX get() = x + SLIDER_START_OFFSET
    private val sliderEndX get() = x + width - SLIDER_END_OFFSET
    private val sliderStartY get() = y + height - 2 * SLIDER_HEIGHT + 1
    private val sliderEndY get() = sliderStartY + SLIDER_HEIGHT

    init {
        sliderWidth =
            floor(sliderMaxWidth * (setting.value.toDouble() / (setting.max.toDouble() - setting.min.toDouble()))).toInt()

        if (sliderWidth > sliderMaxWidth) {
            sliderWidth = sliderMaxWidth
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (dragging) {
            sliderWidth = if (mouseX < sliderStartX) {
                0
            } else if (mouseX > sliderEndX) {
                sliderMaxWidth
            } else {
                mouseX - x - SLIDER_START_OFFSET
            }

            try {
                setting.setAnyValue(
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
                this@SliderElement,
                setting.name,
                offsets.textOffset,
                Color.WHITE
            )
            drawTextRelatively(
                this@SliderElement,
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

            render2D(context) {
                drawEllipse(
                    (sliderStartX + sliderWidth).toFloat() - 2.5f,
                    (sliderStartY + SLIDER_HEIGHT / 2).toFloat() - 2.5f,
                    5f,
                    5f,
                    mainColor
                )
            }
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        if (mouseX in sliderStartX..sliderEndX && mouseY in sliderStartY..sliderEndY) {
            dragging = true
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false
    }
}