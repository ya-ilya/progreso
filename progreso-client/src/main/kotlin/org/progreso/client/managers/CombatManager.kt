package org.progreso.client.managers

import net.minecraft.entity.player.PlayerEntity
import org.progreso.api.Api
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.player.TotemPopEvent
import java.util.concurrent.ConcurrentHashMap

object CombatManager {
    private val pops = ConcurrentHashMap<PlayerEntity, Int>()

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
            if (event.entity is PlayerEntity) {
                pops.remove(event.entity)
            }
        }
    }

    operator fun get(player: PlayerEntity): Int? {
        return pops[player]
    }
}