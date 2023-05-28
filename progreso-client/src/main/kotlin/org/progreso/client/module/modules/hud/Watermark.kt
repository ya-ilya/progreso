package org.progreso.client.module.modules.hud

import org.progreso.client.manager.managers.render.TextRenderManager
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.module.Category
import org.progreso.client.module.HudModule
import org.progreso.client.util.render.Render2DUtil.drawString
import org.progreso.client.util.render.RenderContext
import java.awt.Color

object Watermark : HudModule("Watermark", Category.Hud) {
    private val color by setting("Color", Color.RED)

    override var width = 0; get() = getStringWidth("Progreso Client") + 2
    override var height = 0; get() = TextRenderManager.height + 6

    init {
        x = 10
        y = 10
    }

    override fun render(context: RenderContext) = context {
        drawString(matrices, "Progreso Client", x + 1, y + 2, color)
    }
}