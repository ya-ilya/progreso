package org.progreso.client.module.modules.combat

import net.minecraft.block.Blocks
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import org.progreso.client.events.eventListener
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object Criticals : Module("Criticals", Category.Combat) {
    private val onlyKillAura by setting("OnlyKillAura", false)

    init {
        eventListener<PacketEvent.Send<*>> { event ->
            if (event.packet !is PlayerInteractEntityC2SPacket) return@eventListener
            if (onlyKillAura && !KillAura.enabled) return@eventListener
            if (mc.world.getBlockState(mc.player.blockPos).block == Blocks.COBWEB) return@eventListener

            val x = mc.player.x
            val y = mc.player.y
            val z = mc.player.z

            mc.networkHandler.sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.05, z, false))
            mc.networkHandler.sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false))
            mc.networkHandler.sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.012, z, false))
            mc.networkHandler.sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false))
        }
    }
}