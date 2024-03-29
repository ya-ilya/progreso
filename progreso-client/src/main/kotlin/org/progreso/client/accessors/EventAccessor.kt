package org.progreso.client.accessors

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.event.Event
import org.progreso.api.managers.ModuleManager
import org.progreso.api.managers.PluginManager
import org.progreso.client.Client
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.input.KeyEvent
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.events.safeEventListener

object EventAccessor : EventAccessor {
    override fun register(instance: Any) {
        Client.EVENT_BUS.register(instance)
    }

    override fun unregister(instance: Any) {
        Client.EVENT_BUS.unregister(instance)
    }

    override fun post(event: Event) {
        Client.EVENT_BUS.post(event)
    }

    init {
        register(this)

        safeEventListener<KeyEvent> { event ->
            (ModuleManager.modules + PluginManager.modules)
                .filter { it.bind == event.key }
                .forEach { it.toggle() }
        }

        eventListener<PacketEvent.Receive<*>> { event ->
            if (event.packet is EntityStatusS2CPacket && event.packet.status.toInt() == 35) {
                val entity = event.packet.getEntity(mc.world)

                if (entity is PlayerEntity) {
                    Client.EVENT_BUS.post(TotemPopEvent(entity))
                }
            }
        }

        safeEventListener<TickEvent> { _ ->
            for (entity in mc.world.entities.filterIsInstance<LivingEntity>()) {
                if (entity.deathTime > 0 || entity.health <= 0) {
                    Client.EVENT_BUS.post(EntityDeathEvent(entity))
                }
            }
        }
    }
}