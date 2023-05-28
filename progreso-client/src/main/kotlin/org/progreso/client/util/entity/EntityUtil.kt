package org.progreso.client.util.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import org.progreso.api.managers.FriendManager
import org.progreso.client.Client.Companion.mc

object EntityUtil {
    val Entity.canBeAttacked: Boolean
        get() {
            if (this == mc.player) return false
            if (!isAlive) return false
            if (!isAttackable) return false
            return !(this is PlayerEntity && FriendManager.isFriend(name.string))
        }
}