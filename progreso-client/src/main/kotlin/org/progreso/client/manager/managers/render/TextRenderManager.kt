package org.progreso.client.manager.managers.render

import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.manager.Manager
import org.progreso.client.util.font.CustomFontRenderer
import java.awt.Color
import java.awt.Font

object TextRenderManager : Manager() {
    private val isCustomFont get() = ClickGUI.MODULE.customFont
    private val customFontRenderer = CustomFontRenderer(
        createFont("assets/progreso/font/Barlow-Medium.ttf", 14f)!!,
        antiAlias = true,
        fractionalMetrics = true
    )

    val height: Int
        get() {
            if (isCustomFont) {
                return customFontRenderer.height
            }

            return mc.fontRenderer.FONT_HEIGHT
        }

    fun drawString(text: String, x: Int, y: Int, color: Color) {
        if (isCustomFont) {
            customFontRenderer.drawString(text, x.toFloat(), y.toFloat(), color.rgb)
        } else {
            mc.fontRenderer.drawString(text, x, y, color.rgb)
        }
    }

    fun getStringWidth(text: String): Int {
        return if (isCustomFont) {
            customFontRenderer.getStringWidth(text)
        } else {
            mc.fontRenderer.getStringWidth(text)
        }
    }

    @Suppress("SameParameterValue")
    private fun createFont(resource: String, size: Float): Font? {
        return try {
            Font.createFont(
                Font.PLAIN,
                Thread.currentThread()
                    .contextClassLoader
                    .getResourceAsStream(resource)
            ).deriveFont(size)
        } catch (ex: Exception) {
            null
        }
    }
}