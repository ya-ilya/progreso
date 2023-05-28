package org.progreso.client.events.block

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.progreso.api.event.Event

data class DamageBlockEvent(
    val pos: BlockPos,
    val direction: Direction
) : Event()