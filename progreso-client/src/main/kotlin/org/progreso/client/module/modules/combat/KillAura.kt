package org.progreso.client.module.modules.combat

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.SnowGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.SwordItem
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.entity.EntityUtil.canBeAttacked
import org.progreso.client.util.player.PlayerUtil

object KillAura : Module("KillAura", Category.Combat) {
    private val distance by setting("Distance", 4, 1..6)
    private val onlySword by setting("OnlySword", true)
    private val target by setting("Target", Target.Distance)

    private val targets = setting("Targets")
    private val players by targets.setting("Players", true)
    private val monsters by targets.setting("Monsters", false)
    private val animals by targets.setting("Animals", false)

    init {
        safeEventListener<TickEvent> { _ ->
            if (onlySword && mc.player.mainHandStack.item !is SwordItem) {
                return@safeEventListener
            }

            val entity = mc.world.entities
                .asSequence()
                .filterIsInstance<LivingEntity>()
                .filter { it.canBeAttacked }
                .filter { mc.player.distanceTo(it) <= distance }
                .filter {
                    when (it) {
                        is PlayerEntity -> players
                        is Monster -> monsters
                        is PassiveEntity, is WaterCreatureEntity, is SnowGolemEntity, is IronGolemEntity -> animals
                        else -> false
                    }
                }
                .minByOrNull {
                    when (target) {
                        Target.Distance -> mc.player.distanceTo(it)
                        Target.Health -> it.health
                    }
                }

            if (entity != null) {
                PlayerUtil.attack(entity)
            }
        }
    }

    private enum class Target {
        Distance,
        Health
    }
}