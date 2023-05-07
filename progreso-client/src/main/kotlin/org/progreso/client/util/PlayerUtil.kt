package org.progreso.client.util

import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumHand

object PlayerUtil {
    private val mc: Minecraft = Minecraft.getMinecraft()

    fun attack(entity: EntityLivingBase, checkStrength: Boolean = true, swing: Boolean = true) {
        if (checkStrength && mc.player.getCooledAttackStrength(0f) != 1f) {
            return
        }

        mc.playerController.attackEntity(mc.player, entity)

        if (swing) {
            mc.player.swingArm(EnumHand.MAIN_HAND)
        }
    }
}