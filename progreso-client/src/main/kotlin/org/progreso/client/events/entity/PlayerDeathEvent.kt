package org.progreso.client.events.entity

import net.minecraft.entity.player.PlayerEntity
import org.progreso.api.event.Event

data class PlayerDeathEvent(val player: PlayerEntity) : Event()