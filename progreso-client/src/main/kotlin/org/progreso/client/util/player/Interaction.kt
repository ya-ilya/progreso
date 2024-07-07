package org.progreso.client.util.player

import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand
import org.progreso.client.Client.Companion.mc

fun ClientPlayerInteractionManager.attack(
    entity: LivingEntity,
    checkStrength: Boolean = true,
    tickDelta: Float = 0.5f
) {
    if (checkStrength && mc.player.getAttackCooldownProgress(tickDelta) != 1.0f) {
        return
    }

    attackEntity(mc.player, entity)
    mc.player.swingHand(Hand.MAIN_HAND)
}