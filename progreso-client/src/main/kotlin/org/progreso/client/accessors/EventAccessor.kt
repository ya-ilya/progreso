package org.progreso.client.accessors

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.client.Client
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.entity.PlayerDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.input.CharEvent
import org.progreso.client.events.input.KeyEvent
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.minecraft.ProgresoChatScreen

object EventAccessor : EventAccessor {
    override fun register(instance: Any) {
        Client.EVENT_BUS.register(instance)
    }

    override fun unregister(instance: Any) {
        Client.EVENT_BUS.unregister(instance)
    }

    init {
        register(this)

        safeEventListener<KeyEvent> { event ->
            ModuleManager.onKey(event.key)
        }

        safeEventListener<CharEvent> { event ->
            if (!mc.options!!.sneakKey.isPressed && event.codePoint == CommandManager.PREFIX_CODE && mc.currentScreen !is ProgresoChatScreen) {
                mc.setScreen(ProgresoChatScreen())
                event.isCancelled = true
            }
        }

        eventListener<PacketEvent.Receive<*>> { event ->
            if (event.packet is EntityStatusS2CPacket && event.packet.status.toInt() == 35) {
                val entity = event.packet.getEntity(mc.world)

                if (entity is PlayerEntity) {
                    Client.EVENT_BUS.post(TotemPopEvent(entity))
                }
            }
        }

        eventListener<PacketEvent.Send<*>> { event ->
            if (event.packet is ChatMessageC2SPacket) {
                if (CommandManager.onChat(event.packet.chatMessage)) {
                    event.isCancelled = true
                }
            }
        }

        safeEventListener<TickEvent> { _ ->
            for (player in mc.world.players) {
                if (player.deathTime > 0 || player.health <= 0) {
                    Client.EVENT_BUS.post(PlayerDeathEvent(player))
                }
            }
        }
    }
}