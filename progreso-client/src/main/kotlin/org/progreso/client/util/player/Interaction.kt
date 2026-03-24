package org.progreso.client.util.player

import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import org.progreso.client.Client.Companion.mc

fun MultiPlayerGameMode.attack(
    entity: LivingEntity,
    checkStrength: Boolean = true,
    tickDelta: Float = 1.0f
) {
    val player = mc.player

    if (checkStrength && player.getAttackStrengthScale(tickDelta) < 1.0f) {
        return
    }

    this.attack(player, entity)

    player.swing(InteractionHand.MAIN_HAND)
}