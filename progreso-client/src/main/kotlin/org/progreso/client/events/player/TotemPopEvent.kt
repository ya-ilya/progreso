package org.progreso.client.events.player

import net.minecraft.entity.player.EntityPlayer
import org.progreso.api.event.Event
import org.progreso.client.manager.managers.minecraft.CombatManager

data class TotemPopEvent(val player: EntityPlayer) : Event() {
    val count get() = CombatManager[player]
}