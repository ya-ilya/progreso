package org.progreso.client.util.player

import net.minecraft.entity.LivingEntity
import net.minecraft.util.Hand
import org.progreso.client.Client.Companion.mc

object PlayerUtil {
    fun attack(entity: LivingEntity, checkStrength: Boolean = true) {
        if (checkStrength && mc.player!!.getAttackCooldownProgress(mc.tickDelta) != 1.0f) {
            return
        }

        mc.interactionManager!!.attackEntity(mc.player, entity)
        mc.player!!.swingHand(Hand.MAIN_HAND)
    }
}