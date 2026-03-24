package org.progreso.client.modules.combat

import net.minecraft.network.protocol.game.ServerboundInteractPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.level.block.Blocks
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.eventListener
import org.progreso.client.events.network.PacketEvent

@AbstractModule.AutoRegister
object Criticals : AbstractModule() {
    private val onlyKillAura by setting("OnlyKillAura", false)

    init {
        eventListener<PacketEvent.Send<*>> { event ->
            val packet = event.packet

            if (packet !is ServerboundInteractPacket) return@eventListener
            if (onlyKillAura && !KillAura.enabled) return@eventListener

            val player = mc.player
            val level = mc.level
            val connection = mc.connection ?: return@eventListener

            if (!player.onGround() || level.getBlockState(player.blockPosition())
                    .`is`(Blocks.COBWEB)
            ) return@eventListener

            val x = player.x
            val y = player.y
            val z = player.z

            connection.send(ServerboundMovePlayerPacket.Pos(x, y + 0.05, z, false, false))
            connection.send(ServerboundMovePlayerPacket.Pos(x, y, z, false, false))
            connection.send(ServerboundMovePlayerPacket.Pos(x, y + 0.012, z, false, false))
            connection.send(ServerboundMovePlayerPacket.Pos(x, y, z, false, false))
        }
    }
}