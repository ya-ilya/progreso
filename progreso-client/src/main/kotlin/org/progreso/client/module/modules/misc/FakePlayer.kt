package org.progreso.client.module.modules.misc

import com.mojang.authlib.GameProfile
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.entity.Entity
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import java.util.*

object FakePlayer : Module("FakePlayer", Category.Misc) {
    private val fakePlayerName by setting("Name", "FakePlayer")
    private var fakePlayer: OtherClientPlayerEntity? = null

    init {
        onEnable {
            if (mc.player == null || mc.world == null) {
                toggle()
                return@onEnable
            }

            fakePlayer = OtherClientPlayerEntity(mc.world, GameProfile(UUID.randomUUID(), fakePlayerName))
            fakePlayer!!.copyFrom(mc.player)
            mc.world!!.addEntity(-1, fakePlayer)
        }

        onDisable {
            if (mc.player == null || mc.world == null || fakePlayer == null) return@onDisable

            mc.world!!.removeEntity(fakePlayer!!.id, Entity.RemovalReason.DISCARDED)
        }
    }
}