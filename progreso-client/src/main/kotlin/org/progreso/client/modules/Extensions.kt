package org.progreso.client.modules

import net.minecraft.client.gui.DrawContext
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.Render2DEvent
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.gui.drawRect
import org.progreso.client.modules.client.ClickGUI

fun AbstractHudModule.render(block: DrawContext.() -> Unit) {
    eventListener<Render2DEvent> { event ->
        if (mc.currentScreen is HudEditor) {
            event.context.drawRect(x, y, width, height, ClickGUI.rectColor)
        }

        block(event.context)
    }
}