package org.progreso.client.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.manager.managers.render.TextRenderManager
import java.awt.Color

operator fun DrawContext.invoke(block: ContextWrapper.() -> Unit) {
    ContextWrapper(this).apply(block)
}

fun <S> DrawContext.invokeSuper(superRef: S, block: ContextWrapper.(S) -> Unit) {
    ContextWrapper(this).also { block(it, superRef) }
}

fun DrawContext.drawText(text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
    drawText(mc.textRenderer, text, x, y, color.rgb, shadow)
}

fun DrawContext.drawText(text: Text, x: Int, y: Int, color: Color, shadow: Boolean = false) {
    drawText(mc.textRenderer, text, x, y, color.rgb, shadow)
}

fun DrawContext.drawRect(x: Int, y: Int, width: Int, height: Int, color: Color) {
    fill(x, y, x + width, y + height, color.rgb)
}

fun DrawContext.drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
    drawBorder(x, y, width, height, color.rgb)
}

fun DrawContext.drawBorderedRect(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    fillColor: Color,
    borderColor: Color
) {
    drawRect(x, y, width, height, fillColor)
    drawBorder(x, y, width, height, borderColor)
}

fun DrawContext.drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
    fill(x, startY + 1, x + 1, endY, color.rgb)
}

fun DrawContext.drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
    fill(startX, y, endX + 1, y + 1, color.rgb)
}

class ContextWrapper(private val context: DrawContext) {
    fun drawText(text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
        context.drawText(text, x, y, color, shadow)
    }

    fun drawRect(x: Int, y: Int, width: Int, height: Int, color: Color) {
        context.drawRect(x, y, width, height, color)
    }

    fun drawBorder(x: Int, y: Int, width: Int, height: Int, color: Color) {
        context.drawBorder(x, y, width, height, color)
    }

    fun drawBorderedRect(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        fillColor: Color,
        borderColor: Color
    ) {
        context.drawBorderedRect(x, y, width, height, fillColor, borderColor)
    }

    fun drawVerticalLine(x: Int, startY: Int, endY: Int, color: Color) {
        context.drawVerticalLine(x, startY, endY, color)
    }

    fun drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
        context.drawHorizontalLine(startX, endX, y, color)
    }

    fun AbstractComponent.drawStringRelatively(text: String, xOffset: Int, yOffset: Int, color: Color) {
        context.drawText(text, x + xOffset, y + yOffset, color)
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
        context.drawText(
            text,
            x + width.div(2) - TextRenderManager.getStringWidth(text).div(2),
            y + height.div(2) - TextRenderManager.height.div(2),
            color
        )
    }
}