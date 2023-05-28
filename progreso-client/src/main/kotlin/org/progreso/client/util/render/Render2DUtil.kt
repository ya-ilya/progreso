package org.progreso.client.util.render

import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.manager.managers.render.TextRenderManager
import java.awt.Color

@Suppress("MemberVisibilityCanBePrivate", "unused")
object Render2DUtil {
    val Color.glColors: List<Float>
        get() = listOf(
            (rgb shr 16 and 0xFF) / 255.0f,
            (rgb shr 8 and 0xFF) / 255.0f,
            (rgb and 0xFF) / 255.0f,
            (rgb shr 24 and 0xFF) / 255.0f,
        )

    fun drawRect(matrices: MatrixStack, x: Int, y: Int, width: Int, height: Int, color: Color) {
        DrawableHelper.fill(matrices, x, y, x + width, y + height, color.rgb)
    }

    fun drawBorder(matrices: MatrixStack, x: Int, y: Int, width: Int, height: Int, color: Color) {
        DrawableHelper.drawBorder(matrices, x, y, width, height, color.rgb)
    }

    fun drawVerticalLine(matrices: MatrixStack, x: Int, startY: Int, endY: Int, color: Color) {
        DrawableHelper.fill(matrices, x, startY + 1, x + 1, endY, color.rgb)
    }

    fun drawHorizontalLine(matrices: MatrixStack, startX: Int, endX: Int, y: Int, color: Color) {
        DrawableHelper.fill(matrices, startX, y, endX + 1, y + 1, color.rgb)
    }

    fun drawBorderedRect(
        matrices: MatrixStack,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        fillColor: Color,
        borderColor: Color
    ) {
        drawRect(matrices, x, y, width, height, fillColor)
        drawBorder(matrices, x, y, width, height, borderColor)
    }

    fun drawString(matrices: MatrixStack, text: Any, x: Int, y: Int, color: Color) {
        TextRenderManager.drawString(matrices, text.toString(), x, y, color)
    }

    fun AbstractComponent.drawStringRelatively(
        matrices: MatrixStack,
        text: String,
        xOffset: Int,
        yOffset: Int,
        color: Color
    ) {
        drawString(matrices, text, x + xOffset, y + yOffset, color)
    }

    fun AbstractComponent.drawStringRelatively(matrices: MatrixStack, text: String, xOffset: Int, color: Color) {
        drawStringRelatively(
            matrices,
            text,
            xOffset,
            height.div(2) - TextRenderManager.height.div(2),
            color
        )
    }

    fun AbstractComponent.drawCenteredString(matrices: MatrixStack, text: String, color: Color) {
        drawString(
            matrices,
            text,
            x + width.div(2) - TextRenderManager.getStringWidth(text).div(2),
            y + height.div(2) - TextRenderManager.height.div(2),
            color
        )
    }
}