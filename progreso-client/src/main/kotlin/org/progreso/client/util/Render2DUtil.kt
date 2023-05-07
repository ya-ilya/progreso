package org.progreso.client.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.manager.managers.render.TextRenderManager
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin


@Suppress("MemberVisibilityCanBePrivate", "unused")
object Render2DUtil {
    private val mc: Minecraft = Minecraft.getMinecraft()

    val Color.glColors: List<Float>
        get() = listOf(
            (rgb shr 16 and 0xFF) / 255.0f,
            (rgb shr 8 and 0xFF) / 255.0f,
            (rgb and 0xFF) / 255.0f,
            (rgb shr 24 and 0xFF) / 255.0f,
        )

    fun drawRoundedRect(xI: Int, yI: Int, width: Int, height: Int, radius: Int, color: Color) {
        val (red, green, blue, alpha) = color.glColors
        val startX = xI.toDouble() * 2
        val startY = yI.toDouble() * 2
        val endX = (startX + width) * 2
        val endY = (startY + height) * 2

        GlStateManager.pushMatrix()
        GL11.glPushAttrib(0)
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.enableBlend()
        GlStateManager.color(red, green, blue, alpha)
        GlStateManager.disableTexture2D()
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GlStateManager.glBegin(GL11.GL_POLYGON)

        for (i in 0..90 step 3) {
            GL11.glVertex2d(
                startX + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                startY + radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
        }

        for (i in 90..180 step 3) {
            GL11.glVertex2d(
                startX + radius + sin(i * Math.PI / 180.0) * radius * -1.0,
                endY - radius + cos(i * Math.PI / 180.0) * radius * -1.0
            )
        }

        for (i in 0..90 step 3) {
            GL11.glVertex2d(
                endX - radius + sin(i * Math.PI / 180.0) * radius,
                endY - radius + cos(i * Math.PI / 180.0) * radius
            )
        }

        for (i in 90..180 step 3) {
            GL11.glVertex2d(
                endX - radius + sin(i * Math.PI / 180.0) * radius,
                startY + radius + cos(i * Math.PI / 180.0) * radius
            )
        }

        GlStateManager.glEnd()
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.enableTexture2D()
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.scale(2f, 2f, 2f)
        GlStateManager.popAttrib()
        GlStateManager.popMatrix()
    }

    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Color) {
        Gui.drawRect(x, y, x + width, y + height, color.rgb)
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
        drawHorizontalLine(x, x + width, y, color)
        drawHorizontalLine(x, x + width, y + height, color)
        drawVerticalLine(x, y, y + height, color)
        drawVerticalLine(x + width, y, y + height, color)
    }

    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
        Gui.drawRect(x, startY + 1, x + 1, endY, color.rgb)
    }

    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
        Gui.drawRect(startX, y, endX + 1, y + 1, color.rgb)
    }

    fun drawBorderedRect(x: Int, y: Int, width: Int, height: Int, fillColor: Color, borderColor: Color) {
        drawRect(x, y, width, height, fillColor)
        drawBorder(x, y, width, height, borderColor)
    }

    fun drawString(text: Any, x: Int, y: Int, color: Color) {
        TextRenderManager.drawString(text.toString(), x, y, color)
    }

    fun AbstractComponent.drawStringRelatively(text: String, xOffset: Int, yOffset: Int, color: Color) {
        drawString(text, x + xOffset, y + yOffset, color)
    }

    fun AbstractComponent.drawStringRelatively(text: String, xOffset: Int, color: Color) {
        drawStringRelatively(
            text,
            xOffset,
            height.div(2) - TextRenderManager.height.div(2),
            color
        )
    }

    fun AbstractComponent.drawCenteredString(text: String, color: Color) {
        drawString(
            text,
            x + width.div(2) - TextRenderManager.getStringWidth(text).div(2),
            y + height.div(2) - TextRenderManager.height.div(2),
            color
        )
    }
}