package org.progreso.client.modules.combat

import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket
import net.minecraft.text.Text
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.modules.misc.FakePlayer

@AbstractModule.AutoRegister
object AutoLog : AbstractModule() {
    private val range by setting("Range", true)
    private val rangeValue by setting("RangeValue", 5, 1..128) { range }

    private val health by setting("Health", false)
    private val healthValue by setting("HealthValue", 5, 1..18) { health }

    init {
        safeEventListener<TickEvent> {
            if (range) {
                val playersInRange = mc.world.players
                    .filter { mc.player.distanceTo(it) <= rangeValue }
                    .filter { mc.player != it && FakePlayer.fakePlayer != it }
                    .map { it.name.string }

                if (playersInRange.isNotEmpty()) {
                    mc.player.networkHandler.onDisconnect(
                        DisconnectS2CPacket(Text.of("[AutoLog] Players in range: ${playersInRange.joinToString()}"))
                    )
                }
            }

            if (health) {
                if (mc.player.health <= healthValue) {
                    mc.player.networkHandler.onDisconnect(
                        DisconnectS2CPacket(Text.of("[AutoLog] Health <= $healthValue"))
                    )
                }
            }
        }
    }
}