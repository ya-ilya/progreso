package org.progreso.client.modules.misc

import com.mojang.authlib.GameProfile
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.entity.Entity
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import java.util.*

@AbstractModule.Register("FakePlayer", Category.Misc)
object FakePlayer : AbstractModule() {
    private val fakePlayerName by setting("Name", "FakePlayer")
    private var fakePlayer: OtherClientPlayerEntity? = null

    init {
        onEnable {
            if (mc.isNotSafe()) {
                toggle()
                return@onEnable
            }

            fakePlayer = OtherClientPlayerEntity(mc.world, GameProfile(UUID.randomUUID(), fakePlayerName))
            fakePlayer!!.copyFrom(mc.player)
            mc.world.addEntity(-1, fakePlayer)
        }

        onDisable {
            if (mc.isNotSafe() || fakePlayer == null) return@onDisable

            mc.world.removeEntity(fakePlayer!!.id, Entity.RemovalReason.DISCARDED)
        }
    }
}