package org.progreso.client.gui.clickgui.component.components

import org.lwjgl.opengl.GL11
import org.progreso.api.setting.settings.ColorSetting
import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.util.Render2DUtil.drawRect
import org.progreso.client.util.Render2DUtil.drawStringRelatively
import org.progreso.client.util.Render2DUtil.glColors
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class ColorComponent(
    private val setting: ColorSetting,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
    private companion object {
        const val PICKER_HEIGHT = 40
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

    private val hueSetting = object : NumberSetting<Float>("Hue", hsbValue[0] * 255f, 0f..255f) {
        override fun valueChanged(oldValue: Float, newValue: Float) {
            setHue(newValue / 255f)
        }
    }

    private val alphaSetting = object : NumberSetting<Int>("Alpha", setting.value.alpha, 0..255) {
        override fun valueChanged(oldValue: Int, newValue: Int) {
            setting.value = setting.value.copy(alpha = newValue)
        }
    }

    override val visible get() = setting.visibility()

    init {
        listComponents.add(object : ChildComponent(height, this@ColorComponent) {
            private var picking = false

            init {
                this.height = PICKER_HEIGHT
            }

            override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawComponent(mouseX, mouseY, partialTicks)

                if (picking && isHover(mouseX, mouseY)) {
                    val restrictedX = min(max(x, mouseX), x + width).toFloat()
                    val restrictedY = min(max(y, mouseY), y + this.height).toFloat()
                    setSaturation((restrictedX - x.toFloat()) / width)
                    setBrightness(1 - (restrictedY - y.toFloat()) / this.height)
                    pickerX = mouseX - x
                    pickerY = mouseY - y
                }

                drawPicker(x.toFloat(), y.toFloat(), width.toFloat(), this.height.toFloat(), setting.value)

                if (pickerX != -1 && pickerY != -1) {
                    drawRect(x + pickerX - 1, y + pickerY - 1, 2, 2, Color.WHITE)
                }
            }

            override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
                super.mouseClicked(mouseX, mouseY, mouseButton)

                picking = true
            }

            override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
                super.mouseReleased(mouseX, mouseY, state)

                picking = false
            }

            @Suppress("SameParameterValue")
            private fun drawPicker(x: Float, y: Float, width: Float, height: Float, color: Color) {
                val (red, green, blue, alpha) = color.glColors

                GL11.glPushMatrix()
                GL11.glPushAttrib(GL11.GL_CURRENT_BIT)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                GL11.glShadeModel(GL11.GL_SMOOTH)
                GL11.glBegin(GL11.GL_POLYGON)
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                GL11.glVertex2f(x, y)
                GL11.glVertex2f(x, y + height)
                GL11.glColor4f(red, green, blue, alpha)
                GL11.glVertex2f(x + width, y + height)
                GL11.glVertex2f(x + width, y)
                GL11.glEnd()
                GL11.glDisable(GL11.GL_ALPHA_TEST)
                GL11.glBegin(GL11.GL_POLYGON)
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f)
                GL11.glVertex2f(x, y)
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f)
                GL11.glVertex2f(x, y + height)
                GL11.glVertex2f(x + width, y + height)
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f)
                GL11.glVertex2f(x + width, y)
                GL11.glEnd()
                GL11.glEnable(GL11.GL_ALPHA_TEST)
                GL11.glShadeModel(GL11.GL_FLAT)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glDisable(GL11.GL_BLEND)
                GL11.glPopAttrib()
                GL11.glPopMatrix()
            }
        })

        listComponents.add(SliderComponent(hueSetting, height, this))
        listComponents.add(SliderComponent(alphaSetting, height, this))

        header = object : ChildComponent(height, this@ColorComponent) {
            override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawComponent(mouseX, mouseY, partialTicks)

                drawStringRelatively(
                    setting.name,
                    offsets.textOffset,
                    Color.WHITE
                )

                drawRect(
                    x + width - 9,
                    y + height.div(2) - 2,
                    4, 4, setting.value
                )
            }
        }
    }

    private fun setHue(hue: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hue, hsb[1], hsb[2])).copy(alpha = alpha)
    }

    private fun setSaturation(saturation: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hsb[0], saturation, hsb[2])).copy(alpha = alpha)
    }

    private fun setBrightness(brightness: Float) {
        val hsb = hsbValue
        val alpha = setting.value.alpha
        setting.value = Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness)).copy(alpha = alpha)
    }

    private fun Color.copy(red: Int? = null, green: Int? = null, blue: Int? = null, alpha: Int? = null): Color {
        return Color(red ?: this.red, green ?: this.green, blue ?: this.blue, alpha ?: this.alpha)
    }
}