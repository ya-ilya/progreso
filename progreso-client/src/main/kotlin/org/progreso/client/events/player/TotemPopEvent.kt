package org.progreso.client.events.player

import net.minecraft.world.entity.player.Player
import org.progreso.api.event.Event
import org.progreso.client.managers.CombatManager

data class TotemPopEvent(val player: Player) : Event() {
    val count get() = CombatManager[player]
}