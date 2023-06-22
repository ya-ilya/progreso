package org.progreso.client.module.modules.hud

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.invoke
import org.progreso.client.module.Category
import org.progreso.client.module.HudModule
import java.awt.Color

object Watermark : HudModule("Watermark", Category.Hud) {
    private val color by setting("Color", Color.RED)

    override var width = 0; get() = mc.textRenderer.getWidth("Progreso Client") + 2
    override var height = 0; get() = mc.textRenderer.fontHeight + 6

    init {
        x = 10
        y = 10
    }

    override fun render(context: DrawContext) = context {
        drawText("Progreso Client", x + 1, y + 2, color)
    }
}