package org.progreso.client.module

import net.minecraft.client.MinecraftClient
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.Client
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.RenderOverlayEvent
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.module.modules.client.ClickGUI
import org.progreso.client.util.render.RenderContext

abstract class HudModule(
    name: String,
    description: String,
    category: Category
) : AbstractHudModule(name, description, category) {
    constructor(name: String, category: Category) : this(name, "", category)

    protected companion object {
        val mc: MinecraftClient by lazy { Client.mc }
    }

    init {
        eventListener<RenderOverlayEvent> { event ->
            if (mc.currentScreen is HudEditor) {
                event.context.drawRect(x, y, width, height, ClickGUI.rectColor)
            }

            render(event.context)
        }
    }

    abstract fun render(context: RenderContext)
}