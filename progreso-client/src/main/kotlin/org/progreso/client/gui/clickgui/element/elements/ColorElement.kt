package org.progreso.client.gui.clickgui.element.elements

import net.minecraft.client.gui.DrawContext
import org.progreso.api.setting.settings.ColorSetting
import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.AbstractChildListElement
import org.progreso.client.gui.clickgui.element.ParentElement
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.drawTextRelatively
import org.progreso.client.gui.invoke
import org.progreso.client.util.render.drawEllipse
import org.progreso.client.util.render.drawPicker
import org.progreso.client.util.render.render2D
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class ColorElement(
    private val setting: ColorSetting,
    height: Int,
    parent: ParentElement
) : AbstractChildListElement(height, parent) {
    companion object {
        private const val PICKER_HEIGHT = 40

        fun Color.copy(alpha: Int): Color {
            return Color(red, green, blue, alpha)
        }
    }

    private var pickerX = -1
    private var pickerY = -1

    private val hsbValue
        get() = Color.RGBtoHSB(
            setting.value.red,
            setting.value.green,
            setting.value.blue,
            null
        )

    private val hueSetting = NumberSetting("Hue", hsbValue[0] * 255f, 0f..255f).apply {
        valueChanged { _, newValue ->
            setHue(newValue / 255f)
        }
    }

    private val alphaSetting = NumberSetting("Alpha", setting.value.alpha, 0..255).apply {
        valueChanged { _, newValue ->
            setting.value = setting.value.copy(newValue)
        }
    }

    override val visible get() = setting.visibility()

    init {
        listElements.add(object : AbstractChildElement(height, this@ColorElement) {
            private var picking = false

            init {
                this.height = PICKER_HEIGHT
            }

            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                if (picking && isHover(mouseX, mouseY)) {
                    val restrictedX = min(max(x, mouseX), x + width).toFloat()
                    val restrictedY = min(max(y, mouseY), y + this.height).toFloat()
                    setSaturation((restrictedX - x.toFloat()) / width)
                    setBrightness(1 - (restrictedY - y.toFloat()) / this.height)
                    pickerX = mouseX - x
                    pickerY = mouseY - y
                }

                val headerHeight = this.height

                render2D(context) {
                    drawPicker(
                        x.toFloat(),
                        y.toFloat(),
                        width.toFloat(),
                        headerHeight.toFloat(),
                        setting.value
                    )
                }

                context {
                    if (pickerX != -1 && pickerY != -1) {
                        drawRect(x + pickerX - 1, y + pickerY - 1, 2, 2, Color.WHITE)
                    }
                }
            }

            override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
                super.mouseClicked(mouseX, mouseY, button)

                picking = true
            }

            override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
                super.mouseReleased(mouseX, mouseY, state)

                picking = false
            }
        })

        listElements.add(SliderElement(hueSetting, height, this))
        listElements.add(SliderElement(alphaSetting, height, this))

        header = object : AbstractChildElement(height, this@ColorElement) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                context.drawTextRelatively(
                    this,
                    setting.name,
                    offsets.textOffset,
                    Color.WHITE
                )

                render2D(context) {
                    drawEllipse(
                        (x + width - 9).toFloat() - 2.5f,
                        (y + height.div(2)).toFloat() - 2.5f,
                        5f,
                        5f,
                        setting.value
                    )
                }
            }
        }
    }

    private fun setHue(hue: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hue, hsb[1], hsb[2])).copy(alpha)
    }

    private fun setSaturation(saturation: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hsb[0], saturation, hsb[2])).copy(alpha)
    }

    private fun setBrightness(brightness: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness)).copy(alpha)
    }
}