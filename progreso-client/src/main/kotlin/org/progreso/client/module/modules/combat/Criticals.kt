package org.progreso.client.module.modules.combat

import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketUseEntity
import org.progreso.api.managers.ModuleManager
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.mixin.mixins.accessors.IEntity
import org.progreso.client.module.Category
import org.progreso.client.module.Module

@Suppress("SpellCheckingInspection")
class Criticals : Module("Criticals", Category.Combat) {
    companion object {
        private val KILL_AURA = ModuleManager[KillAura::class]
    }

    private val onlyKillAura by setting("OnlyKillAura", false)

    init {
        eventListener<PacketEvent.Send<*>> { event ->
            if (event.packet !is CPacketUseEntity) return@eventListener
            if (event.packet.action != CPacketUseEntity.Action.ATTACK) return@eventListener
            if (onlyKillAura && !KILL_AURA.enabled) return@eventListener
            if (mc.player.isInWater || mc.player.isInLava || (mc.player as IEntity).isInWeb) return@eventListener

            val entity = event.packet.getEntityFromWorld(mc.world)

            if (entity is EntityLivingBase) {
                val x = mc.player.posX
                val y = mc.player.posY
                val z = mc.player.posZ

                mc.player.connection.sendPacket(CPacketPlayer.Position(x, y + 0.05, z, false))
                mc.player.connection.sendPacket(CPacketPlayer.Position(x, y, z, false))
                mc.player.connection.sendPacket(CPacketPlayer.Position(x, y + 0.012, z, false))
                mc.player.connection.sendPacket(CPacketPlayer.Position(x, y, z, false))
                mc.player.onCriticalHit(entity)
            }
        }
    }
}