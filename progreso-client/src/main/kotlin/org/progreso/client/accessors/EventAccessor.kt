package org.progreso.client.accessors

import net.minecraft.network.protocol.game.ClientboundEntityEventPacket
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
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
            val packet = event.packet
            if (packet is ClientboundEntityEventPacket && packet.eventId.toInt() == 35) {
                val entity = packet.getEntity(mc.level)

                if (entity is Player) {
                    Client.EVENT_BUS.post(TotemPopEvent(entity))
                }
            }
        }

        safeEventListener<TickEvent> { _ ->
            for (entity in mc.level.entitiesForRendering().filterIsInstance<LivingEntity>()) {
                if (entity.deathTime > 0 || entity.health <= 0f) {
                    Client.EVENT_BUS.post(EntityDeathEvent(entity))
                }
            }
        }
    }
}