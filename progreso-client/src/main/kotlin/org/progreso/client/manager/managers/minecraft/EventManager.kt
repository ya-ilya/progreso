package org.progreso.client.manager.managers.minecraft

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.SPacketEntityStatus
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.api.event.EventHandler
import org.progreso.client.Client
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.manager.Manager

object EventManager : Manager(events = true) {
    @EventHandler
    fun onPacketReceive(event: PacketEvent.Receive<*>) {
        if (event.packet is SPacketEntityStatus && event.packet.opCode.toInt() == 35) {
            val entity = event.packet.getEntity(mc.world)

            if (entity is EntityPlayer) {
                Client.EVENT_BUS.post(TotemPopEvent(entity))
            }
        }
    }

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        event.isCanceled = Client.EVENT_BUS.post(EntityDeathEvent(event.entity))
    }
}