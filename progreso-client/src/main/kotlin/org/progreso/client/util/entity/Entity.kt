package org.progreso.client.util.entity

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import org.progreso.api.managers.FriendManager
import org.progreso.client.Client

val Entity.canBeAttacked: Boolean
    get() {
        if (this == Client.mc.player) return false
        if (!isAlive) return false
        if (!isAttackable) return false
        return !(this is Player && FriendManager.isFriend(name.string))
    }