package org.progreso.client.events.block

import net.minecraft.util.math.BlockPos
import org.progreso.api.event.Event

data class DestroyBlockEvent(val pos: BlockPos) : Event()