package org.progreso.client.managers

import net.minecraft.world.entity.player.Player
import org.progreso.api.Api
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.player.TotemPopEvent
import java.util.concurrent.ConcurrentHashMap

object CombatManager {
    private val pops = ConcurrentHashMap<Player, Int>()

    init {
        Api.EVENT.register(this)

        eventListener<TotemPopEvent> { event ->
            pops[event.player] = if (pops.containsKey(event.player)) {
                pops[event.player]!! + 1
            } else {
                1
            }
        }

        eventListener<EntityDeathEvent> { event ->
            if (event.entity is Player) {
                pops.remove(event.entity)
            }
        }
    }

    operator fun get(player: Player): Int? {
        return pops[player]
    }
}