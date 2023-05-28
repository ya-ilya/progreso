package org.progreso.client.util.render

import net.minecraft.client.util.math.MatrixStack
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.util.render.Render2DUtil.drawCenteredString
import org.progreso.client.util.render.Render2DUtil.drawStringRelatively
import java.awt.Color

class RenderContext(val matrices: MatrixStack) {
    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Color) {
        Render2DUtil.drawRect(matrices, x, y, width, height, color)
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
        Render2DUtil.drawBorder(matrices, x, y, width, height, color)
    }

    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
        Render2DUtil.drawVerticalLine(matrices, x, startY, endY, color)
    }

    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
        Render2DUtil.drawHorizontalLine(matrices, startX, endX, y, color)
    }

    fun drawBorderedRect(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        fillColor: Color,
        borderColor: Color
    ) {
        Render2DUtil.drawBorderedRect(matrices, x, y, width, height, fillColor, borderColor)
    }

    fun drawString(text: Any, x: Int, y: Int, color: Color) {
        Render2DUtil.drawString(matrices, text, x, y, color)
    }

    fun AbstractComponent.drawStringRelatively(text: String, xOffset: Int, yOffset: Int, color: Color) {
        drawStringRelatively(matrices, text, xOffset, yOffset, color)
    }

    fun AbstractComponent.drawStringRelatively(text: String, xOffset: Int, color: Color) {
        drawStringRelatively(matrices, text, xOffset, color)
    }

    fun AbstractComponent.drawCenteredString(text: String, color: Color) {
        drawCenteredString(matrices, text, color)
    }


    operator fun <T> invoke(block: RenderContext.() -> T): T {
        return block(this)
    }

    fun <T, S> invokeSuper(_super: S, block: RenderContext.(S) -> T): T {
        return block(this, _super)
    }
}