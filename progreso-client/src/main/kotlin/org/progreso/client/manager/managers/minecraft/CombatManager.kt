package org.progreso.client.manager.managers.minecraft

import net.minecraft.entity.player.PlayerEntity
import org.progreso.client.events.entity.PlayerDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.manager.Manager
import java.util.concurrent.ConcurrentHashMap

object CombatManager : Manager(events = true) {
    private val pops = ConcurrentHashMap<PlayerEntity, Int>()

    init {
        eventListener<TotemPopEvent> { event ->
            pops[event.player] = if (pops.containsKey(event.player)) {
                pops[event.player]!! + 1
            } else {
                1
            }
        }

        eventListener<PlayerDeathEvent> {
            pops.remove(it.player)
        }
    }

    operator fun get(player: PlayerEntity): Int? {
        return pops[player]
    }
}