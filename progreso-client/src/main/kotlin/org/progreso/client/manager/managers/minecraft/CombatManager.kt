package org.progreso.client.manager.managers.minecraft

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.manager.Manager
import java.util.concurrent.ConcurrentHashMap

object CombatManager : Manager(events = true) {
    private val pops = ConcurrentHashMap<EntityPlayer, Int>()

    init {
        eventListener<TotemPopEvent> { event ->
            pops[event.player] = if (pops.containsKey(event.player)) {
                pops[event.player]!! + 1
            } else {
                1
            }
        }
    }

    operator fun get(player: EntityPlayer): Int? {
        return pops[player]
    }

    @SubscribeEvent
    fun onDeath(event: LivingDeathEvent) {
        if (event.entity is EntityPlayer) {
            pops.remove(event.entity)
        }
    }
}