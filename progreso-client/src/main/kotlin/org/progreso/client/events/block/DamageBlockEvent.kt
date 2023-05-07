package org.progreso.client.events.block

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import org.progreso.api.event.Event

data class DamageBlockEvent(
    val pos: BlockPos,
    val facing: EnumFacing
) : Event()