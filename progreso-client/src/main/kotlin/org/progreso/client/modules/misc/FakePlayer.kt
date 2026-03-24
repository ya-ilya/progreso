package org.progreso.client.modules.misc

import com.mojang.authlib.GameProfile
import net.minecraft.client.player.RemotePlayer
import net.minecraft.world.entity.Entity
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import java.util.*

@AbstractModule.AutoRegister
object FakePlayer : AbstractModule() {
    private val fakePlayerName by setting("Name", "FakePlayer")

    var fakePlayer: RemotePlayer? = null

    init {
        onEnable {
            if (mc.isNotSafe()) {
                toggle()
                return@onEnable
            }

            val level = mc.level
            val player = mc.player

            fakePlayer = RemotePlayer(level, GameProfile(UUID.randomUUID(), fakePlayerName)).apply {
                restoreFrom(player)
                copyPosition(player)

                yHeadRot = player.yHeadRot
                yBodyRot = player.yBodyRot

                id = -1

                level.addEntity(this)
            }
        }

        onDisable {
            if (mc.isNotSafe() || fakePlayer == null) return@onDisable
            mc.level.removeEntity(fakePlayer!!.id, Entity.RemovalReason.DISCARDED)
            fakePlayer = null
        }
    }
}