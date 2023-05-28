package org.progreso.client.events.render

import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.event.Event
import org.progreso.client.util.render.RenderContext

data class RenderOverlayEvent(val matrices: MatrixStack) : Event() {
    val context = RenderContext(matrices)
}