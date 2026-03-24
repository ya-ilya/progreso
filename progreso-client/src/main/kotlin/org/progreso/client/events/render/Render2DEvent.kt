package org.progreso.client.events.render

import net.minecraft.client.gui.GuiGraphicsExtractor
import org.progreso.api.event.Event

data class Render2DEvent(val context: GuiGraphicsExtractor) : Event()