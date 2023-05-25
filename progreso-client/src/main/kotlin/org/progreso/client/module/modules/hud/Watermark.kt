package org.progreso.client.module.modules.hud

import org.progreso.client.manager.managers.render.TextRenderManager
import org.progreso.client.manager.managers.render.TextRenderManager.getStringWidth
import org.progreso.client.module.Category
import org.progreso.client.module.HudModule
import org.progreso.client.util.Render2DUtil.drawString
import java.awt.Color

object Watermark : HudModule("Watermark", Category.Hud) {
    private val color by setting("Color", Color.RED)

    init {
        x = 10
        y = 10
        width = getStringWidth("Progreso Client") + 2
        height = TextRenderManager.height + 6
    }

    override fun render() {
        drawString("Progreso Client", x + 1, y + 2, color)
    }
}