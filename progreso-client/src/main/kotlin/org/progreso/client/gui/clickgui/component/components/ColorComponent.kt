package org.progreso.client.gui.clickgui.component.components

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import org.progreso.api.setting.settings.ColorSetting
import org.progreso.api.setting.settings.NumberSetting
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.util.render.Render2DUtil.glColors
import org.progreso.client.util.render.RenderContext
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

            override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                if (picking && isHover(mouseX, mouseY)) {
                    val restrictedX = min(max(x, mouseX), x + width).toFloat()
                    val restrictedY = min(max(y, mouseY), y + this.height).toFloat()
                    setSaturation((restrictedX - x.toFloat()) / width)
                    setBrightness(1 - (restrictedY - y.toFloat()) / this.height)
                    pickerX = mouseX - x
                    pickerY = mouseY - y
                }

                drawPicker(context, x.toFloat(), y.toFloat(), width.toFloat(), this.height.toFloat(), setting.value)

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

            @Suppress("SameParameterValue")
            private fun drawPicker(
                context: RenderContext,
                x: Float,
                y: Float,
                width: Float,
                height: Float,
                color: Color
            ) {
                val (red, green, blue, alpha) = color.glColors
                val matrix = context.matrices.peek().positionMatrix

                context.matrices.push()
                // GL11.glPushAttrib(GL11.GL_CURRENT_BIT)
                RenderSystem.enableBlend()
                // GL11.glDisable(GL11.GL_TEXTURE_2D)
                RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
                // GL11.glShadeModel(GL11.GL_SMOOTH)

                val buffer = Tessellator.getInstance().buffer
                buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

                buffer
                    .vertex(matrix, x, y, 0f)
                    .color(1f, 1f, 1f, 1f)
                    .next()

                buffer
                    .vertex(matrix, x, y + height, 0f)
                    .color(1f, 1f, 1f, 1f)
                    .next()

                buffer
                    .vertex(matrix, x + width, y + height, 0f)
                    .color(red, green, blue, alpha)
                    .next()

                buffer
                    .vertex(matrix, x + width, y, 0f)
                    .color(red, green, blue, alpha)
                    .next()

                Tessellator.getInstance().draw()
                buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

                buffer
                    .vertex(matrix, x, y, 0f)
                    .color(0f, 0f, 0f, 0f)
                    .next()

                buffer
                    .vertex(matrix, x, y + height, 0f)
                    .color(0f, 0f, 0f, 1f)
                    .next()

                buffer
                    .vertex(matrix, x + width, y + height, 0f)
                    .color(0f, 0f, 0f, 1f)
                    .next()

                buffer
                    .vertex(matrix, x + width, y, 0f)
                    .color(0f, 0f, 0f, 0f)
                    .next()

                Tessellator.getInstance().draw()
                // GL11.glEnable(GL11.GL_ALPHA_TEST)
                // GL11.glShadeModel(GL11.GL_FLAT)
                // GL11.glEnable(GL11.GL_TEXTURE_2D)
                // RenderSystem.disableBlend()
                // GL11.glPopAttrib()
                context.matrices.pop()
            }
        })

        listComponents.add(SliderComponent(hueSetting, height, this))
        listComponents.add(SliderComponent(alphaSetting, height, this))

        header = object : ChildComponent(height, this@ColorComponent) {
            override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context {
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