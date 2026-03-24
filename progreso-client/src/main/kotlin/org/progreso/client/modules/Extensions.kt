package org.progreso.client.modules

import net.minecraft.client.gui.GuiGraphicsExtractor
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.Render2DEvent
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.drawText
import org.progreso.client.gui.font
import org.progreso.client.modules.client.ClickGUI
import java.awt.Color

fun AbstractHudModule.render(block: GuiGraphicsExtractor.() -> Unit) {
    eventListener<Render2DEvent> { event ->
        if (mc.screen is HudEditor) {
            event.context.drawRect(x, y, width, height, ClickGUI.rectColor)
        }

        block(event.context)
    }
}

abstract class SimpleTextHudModule(private val text: () -> String) : AbstractHudModule() {
    private val color by setting("Color", Color.RED)

    override var width = 0; get() = font.width(text()) + 2
    override var height = 0; get() = font.lineHeight + 4

    init {
        render {
            drawText(text(), x + 1, y + 2, color, true)
        }
    }
}