package org.progreso.client.modules

import net.minecraft.client.gui.DrawContext
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.Render2DEvent
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.gui.drawRect
import org.progreso.client.gui.drawText
import org.progreso.client.gui.textRenderer
import org.progreso.client.modules.client.ClickGUI
import java.awt.Color

fun AbstractHudModule.render(block: DrawContext.() -> Unit) {
    eventListener<Render2DEvent> { event ->
        if (mc.currentScreen is HudEditor) {
            event.context.drawRect(x, y, width, height, ClickGUI.rectColor)
        }

        block(event.context)
    }
}

abstract class SimpleTextHudModule(private val text: () -> String) : AbstractHudModule() {
    private val color by setting("Color", Color.RED)

    override var width = 0; get() = textRenderer.getWidth(text()) + 2
    override var height = 0; get() = textRenderer.fontHeight + 4

    init {
        render {
            drawText(text(), x + 1, y + 2, color, true)
        }
    }
}