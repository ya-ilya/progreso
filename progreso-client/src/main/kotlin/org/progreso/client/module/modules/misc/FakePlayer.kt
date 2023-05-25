package org.progreso.client.module.modules.misc

import com.mojang.authlib.GameProfile
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.world.GameType
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import java.util.*


object FakePlayer : Module("FakePlayer", Category.Misc) {
    private val fakePlayerName by setting("Name", "FakePlayer")

    init {
        onEnable {
            if (mc.player == null || mc.world == null) {
                toggle()
                return@onEnable
            }

            val fakePlayer = EntityOtherPlayerMP(
                mc.world,
                GameProfile(UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7"), fakePlayerName)
            )

            fakePlayer.copyLocationAndAnglesFrom(mc.player)
            fakePlayer.rotationYawHead = mc.player.rotationYawHead
            fakePlayer.setGameType(GameType.SURVIVAL)
            fakePlayer.health = 20f
            mc.world.addEntityToWorld(-1, fakePlayer)
            fakePlayer.onLivingUpdate()
        }

        onDisable {
            if (mc.player == null || mc.world == null) return@onDisable

            mc.world.removeEntityFromWorld(-1)
        }
    }
}