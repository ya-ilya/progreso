@file:Suppress("MemberVisibilityCanBePrivate", "UnusedReceiverParameter", "unused")

package org.progreso.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import org.progreso.client.Client.Companion.config
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.element.Element
import org.progreso.client.modules.client.ClickGUI
import org.progreso.client.util.render.createTextRenderer
import org.progreso.client.util.render.createTextRendererFromProgresoResource
import java.awt.Color

fun createDefaultTextRenderer(): TextRenderer {
    return createTextRenderer("vitala", 11f)!!
}

var customTextRenderer = run {
    if (config.customFont != null) {
        try {
            return@run createTextRendererFromProgresoResource(
                config.customFont!!.name!!,
                config.customFont!!.size!!
            )!!
        } catch (ex: IllegalArgumentException) {
            config.customFont = null

            ex.printStackTrace()
        }
    }

    createDefaultTextRenderer()
}

val textRenderer get() = if (ClickGUI.customFont) customTextRenderer else mc.textRenderer

val Color.glColors: List<Float>
    get() = listOf(
        (rgb shr 16 and 0xFF) / 255.0f,
        (rgb shr 8 and 0xFF) / 255.0f,
        (rgb and 0xFF) / 255.0f,
        (rgb shr 24 and 0xFF) / 255.0f,
    )

operator fun DrawContext.invoke(block: DrawContext.() -> Unit) {
    this.apply(block)
}

val DrawContext.fontHeight get() = textRenderer.fontHeight

fun DrawContext.drawText(
    text: String,
    x: Int,
    y: Int,
    color: Color,
    shadow: Boolean = true
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

fun DrawContext.drawRect(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    color: Color
) {
    fill(x, y, x + width, y + height, color.rgb)
}

fun DrawContext.drawBorder(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    color: Color
) {
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

fun DrawContext.drawVerticalLine(
    x: Int,
    startY: Int,
    endY: Int,
    color: Color
) {
    fill(x, startY, x + 1, endY, color.rgb)
}

fun DrawContext.drawHorizontalLine(
    startX: Int,
    endX: Int,
    y: Int,
    color: Color
) {
    fill(startX, y, endX, y + 1, color.rgb)
}

fun DrawContext.getTextWidth(string: String): Int {
    return textRenderer.getWidth(string)
}

fun DrawContext.drawTextRelatively(
    element: Element,
    text: String,
    xOffset: Int,
    yOffset: Int,
    color: Color
) {
    drawText(text, element.x + xOffset, element.y + yOffset, color)
}

fun DrawContext.drawTextRelatively(
    element: Element,
    text: String,
    xOffset: Int,
    color: Color
) {
    drawTextRelatively(
        element,
        text,
        xOffset,
        element.height.div(2) - fontHeight.div(2),
        color
    )
}

fun DrawContext.drawCenteredString(
    element: Element,
    text: String,
    color: Color
) {
    drawText(
        text,
        element.x + element.width.div(2) - getTextWidth(text).div(2),
        element.y + element.height.div(2) - fontHeight.div(2),
        color
    )
}