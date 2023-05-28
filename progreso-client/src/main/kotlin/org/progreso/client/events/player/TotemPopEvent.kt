package org.progreso.client.events.player

import net.minecraft.entity.player.PlayerEntity
import org.progreso.api.event.Event
import org.progreso.client.manager.managers.minecraft.CombatManager

data class TotemPopEvent(val player: PlayerEntity) : Event() {
    val count get() = CombatManager[player]
}