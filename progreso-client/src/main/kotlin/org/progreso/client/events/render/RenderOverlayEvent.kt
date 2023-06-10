package org.progreso.client.events.render

import net.minecraft.client.gui.DrawContext
import org.progreso.api.event.Event

data class RenderOverlayEvent(val context: DrawContext) : Event()