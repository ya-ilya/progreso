package org.progreso.client.events.render

import com.mojang.blaze3d.vertex.PoseStack
import org.progreso.api.event.Event

data class Render3DEvent(
    val matrices: PoseStack,
    val tickDelta: Float
) : Event()