package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractHudModule
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.drawText
import org.progreso.client.modules.render
import java.awt.Color

@AbstractModule.AutoRegister
object Watermark : AbstractHudModule() {
    private val color by setting("Color", Color.RED)

    override var width = 0; get() = mc.textRenderer.getWidth("Progreso Client") + 2
    override var height = 0; get() = mc.textRenderer.fontHeight + 6

    init {
        x = 10
        y = 10

        render {
            drawText("Progreso Client", x + 1, y + 2, color)
        }
    }
}