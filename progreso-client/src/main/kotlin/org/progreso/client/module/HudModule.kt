package org.progreso.client.module

import net.minecraft.client.gui.DrawContext
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.Client
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.RenderOverlayEvent
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.gui.drawRect
import org.progreso.client.module.modules.client.ClickGUI

abstract class HudModule(
    name: String,
    description: String,
    category: Category
) : AbstractHudModule(name, description, category) {
    constructor(name: String, category: Category) : this(name, "", category)

    protected companion object {
        val mc by lazy { Client.mc }
    }

    init {
        eventListener<RenderOverlayEvent> { event ->
            if (mc.currentScreen is HudEditor) {
                event.context.drawRect(x, y, width, height, ClickGUI.rectColor)
            }

            render(event.context)
        }
    }

    abstract fun render(context: DrawContext)
}