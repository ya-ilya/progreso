package org.progreso.client.manager.managers.render

import net.minecraft.client.util.math.MatrixStack
import org.progreso.client.manager.Manager
import java.awt.Color

object TextRenderManager : Manager() {

    val height: Int
        get() {
            return mc.textRenderer.fontHeight
        }

    fun drawString(matrices: MatrixStack, text: String, x: Int, y: Int, color: Color) {
        mc.textRenderer.draw(matrices, text, x.toFloat(), y.toFloat(), color.rgb)
    }

    fun getStringWidth(text: String): Int {
        return mc.textRenderer.getWidth(text)
    }
}