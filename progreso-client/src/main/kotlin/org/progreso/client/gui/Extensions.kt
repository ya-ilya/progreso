@file:Suppress("MemberVisibilityCanBePrivate", "UnusedReceiverParameter", "unused")

package org.progreso.client.gui

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import org.progreso.client.Client.Companion.config
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.element.Element
import org.progreso.client.modules.client.ClickGUI
import org.progreso.client.util.render.createTextRenderer
import org.progreso.client.util.render.createTextRendererFromProgresoResource
import java.awt.Color

fun createDefaultFont(): Font {
    return createTextRenderer("vitala", 9f)!!
}

var customFont = run {
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

    createDefaultFont()
}

val font get() = if (ClickGUI.customFont) customFont else mc.font

val Color.glColors: List<Float>
    get() = listOf(
        (rgb shr 16 and 0xFF) / 255.0f,
        (rgb shr 8 and 0xFF) / 255.0f,
        (rgb and 0xFF) / 255.0f,
        (rgb shr 24 and 0xFF) / 255.0f,
    )

operator fun GuiGraphicsExtractor.invoke(block: GuiGraphicsExtractor.() -> Unit) {
    this.apply(block)
}

val GuiGraphicsExtractor.lineHeight get() = font.lineHeight

fun GuiGraphicsExtractor.drawText(
    text: String,
    x: Int,
    y: Int,
    color: Color,
    shadow: Boolean = true
) {
    text(font, text, x, y, color.rgb, shadow)
}

fun GuiGraphicsExtractor.drawText(
    font: Font,
    text: String,
    x: Int,
    y: Int,
    color: Color,
    shadow: Boolean = true
) {
    text(font, text, x, y, color.rgb, shadow)
}

fun GuiGraphicsExtractor.drawRect(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    color: Color
) {
    fill(x, y, x + width, y + height, color.rgb)
}

fun GuiGraphicsExtractor.drawBorder(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    color: Color,
    borderWidth: Int = 1
) {
    drawRect(x, y, width, borderWidth, color)
    drawRect(x, y, borderWidth, height, color)
    drawRect(x + width - borderWidth, y, borderWidth, height, color)
    drawRect(x, y + height - borderWidth, width, borderWidth, color)
}

fun GuiGraphicsExtractor.drawBorderedRect(
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

fun GuiGraphicsExtractor.drawVerticalLine(
    x: Int,
    startY: Int,
    endY: Int,
    color: Color
) {
    fill(x, startY, x + 1, endY, color.rgb)
}

fun GuiGraphicsExtractor.drawHorizontalLine(
    startX: Int,
    endX: Int,
    y: Int,
    color: Color
) {
    fill(startX, y, endX, y + 1, color.rgb)
}

fun GuiGraphicsExtractor.getTextWidth(string: String): Int {
    return font.width(string)
}

fun GuiGraphicsExtractor.drawTextRelatively(
    element: Element,
    text: String,
    xOffset: Int,
    yOffset: Int,
    color: Color
) {
    drawText(text, element.x + xOffset, element.y + yOffset, color)
}

fun GuiGraphicsExtractor.drawTextRelatively(
    element: Element,
    text: String,
    xOffset: Int,
    color: Color
) {
    drawTextRelatively(
        element,
        text,
        xOffset,
        element.height.div(2) - lineHeight.div(2),
        color
    )
}

fun GuiGraphicsExtractor.drawCenteredString(
    element: Element,
    text: String,
    color: Color
) {
    drawText(
        text,
        element.x + element.width.div(2) - getTextWidth(text).div(2),
        element.y + element.height.div(2) - lineHeight.div(2),
        color
    )
}