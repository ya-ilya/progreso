package org.progreso.client.events.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import org.progreso.api.event.Event

data class DamageBlockEvent(
    val pos: BlockPos,
    val direction: Direction
) : Event()