package org.progreso.client.manager.managers.render

import net.minecraft.client.gui.DrawContext
import org.progreso.client.manager.Manager
import java.awt.Color

object TextRenderManager : Manager() {

    val height: Int
        get() {
            return mc.textRenderer.fontHeight
        }

    fun drawString(context: DrawContext, text: String, x: Int, y: Int, color: Color) {
        context.drawText(mc.textRenderer, text, x, y, color.rgb, false)
    }

    fun getStringWidth(text: String): Int {
        return mc.textRenderer.getWidth(text)
    }
}