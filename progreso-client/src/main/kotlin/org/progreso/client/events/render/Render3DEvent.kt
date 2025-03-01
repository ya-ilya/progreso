package org.progreso.client.events.render

import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.event.Event

data class Render3DEvent(
    val matrices: MatrixStack,
    val tickDelta: Float
) : Event()