@file:Suppress("MemberVisibilityCanBePrivate", "UnusedReceiverParameter", "unused")

package org.progreso.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import org.progreso.client.gui.clickgui.element.Element
import org.progreso.client.util.render.TextRendererUtil
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

val textRenderer = TextRendererUtil.createTextRenderer("vitala", 11f)!!

val Color.glColors: List<Float>
    get() = listOf(
        (rgb shr 16 and 0xFF) / 255.0f,
        (rgb shr 8 and 0xFF) / 255.0f,
        (rgb and 0xFF) / 255.0f,
        (rgb shr 24 and 0xFF) / 255.0f,
    )

operator fun DrawContext.invoke(block: ContextWrapper.() -> Unit) {
    ContextWrapper(this).apply(block)
}

fun <S> DrawContext.invokeSuper(superRef: S, block: ContextWrapper.(S) -> Unit) {
    ContextWrapper(this).also { block(it, superRef) }
}

val DrawContext.fontHeight get() = textRenderer.fontHeight

fun DrawContext.drawText(
    text: String,
    x: Int,
    y: Int,
    color: Color,
    shadow: Boolean = false
) {
    drawText(textRenderer, text, x, y, color.rgb, shadow)
}

fun DrawContext.drawText(
    textRenderer: TextRenderer,
    text: String,
    x: Int,
    y: Int,
    color: Color,
    shadow: Boolean = false
) {
    drawText(textRenderer, text, x, y, color.rgb, shadow)
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
    fill(x, startY, x + 1, endY, color.rgb)
}

fun DrawContext.drawHorizontalLine(startX: Int, endX: Int, y: Int, color: Color) {
    fill(startX, y, endX, y + 1, color.rgb)
}

fun DrawContext.drawCircle(
    centerX: Int,
    centerY: Int,
    angleFrom: Double,
    angleTo: Double,
    segments: Int,
    radius: Double,
    color: Color
) {
    val buffer = Tessellator.getInstance().buffer
    val matrix = matrices.peek().positionMatrix

    val angleStep = Math.toRadians(angleTo - angleFrom) / segments

    buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR)
    buffer.vertex(matrix, centerX.toFloat(), centerY.toFloat(), 0f).color(color.rgb).next()

    for (i in segments downTo 0) {
        val theta = Math.toRadians(angleFrom) + i * angleStep
        buffer.vertex(
            matrix,
            (centerX - cos(theta) * radius).toFloat(),
            (centerY - sin(theta) * radius).toFloat(), 0f
        ).color(color.rgb).next()
    }

    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    RenderSystem.setShader(GameRenderer::getPositionColorProgram)

    Tessellator.getInstance().draw()
}

fun DrawContext.getTextWidth(string: String): Int {
    return textRenderer.getWidth(string)
}

class ContextWrapper(private val context: DrawContext) {
    val fontHeight get() = context.fontHeight

    fun drawText(textRenderer: TextRenderer, text: String, x: Int, y: Int, color: Color, shadow: Boolean = false) {
        context.drawText(textRenderer, text, x, y, color, shadow)
    }

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

    fun Element.drawTextRelatively(text: String, xOffset: Int, yOffset: Int, color: Color) {
        context.drawText(text, x + xOffset, y + yOffset, color)
    }

    fun Element.drawTextRelatively(text: String, xOffset: Int, color: Color) {
        drawTextRelatively(
            text,
            xOffset,
            height.div(2) - fontHeight.div(2),
            color
        )
    }

    fun Element.drawCenteredString(text: String, color: Color) {
        context.drawText(
            text,
            x + width.div(2) - context.getTextWidth(text).div(2),
            y + height.div(2) - context.fontHeight.div(2),
            color
        )
    }

    fun drawCircle(
        centerX: Int,
        centerY: Int,
        angleFrom: Double,
        angleTo: Double,
        segments: Int,
        radius: Double,
        color: Color
    ) {
        context.drawCircle(centerX, centerY, angleFrom, angleTo, segments, radius, color)
    }

    fun getTextWidth(string: String): Int {
        return context.getTextWidth(string)
    }
}