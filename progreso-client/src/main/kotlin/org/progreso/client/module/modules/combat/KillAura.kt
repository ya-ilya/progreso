package org.progreso.client.module.modules.combat

import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemSword
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.EntityUtil.EntityType
import org.progreso.client.util.EntityUtil.canBeAttacked
import org.progreso.client.util.EntityUtil.entityType
import org.progreso.client.util.PlayerUtil

class KillAura : Module("KillAura", Category.Combat) {
    private val distance by setting("Distance", 4, 1..6)
    private val onlySword by setting("OnlySword", true)
    private val target by setting("Target", Target.Distance)

    private val targets = setting("Targets")
    private val players by targets.setting("Players", true)
    private val monsters by targets.setting("Monsters", false)
    private val animals by targets.setting("Animals", false)
    private val entities by targets.setting("Entities", false)
    private val vehicles by targets.setting("Vehicles", false)
    private val boss by targets.setting("Boss", false)
    private val other by targets.setting("Other", false)

    init {
        safeEventListener<TickEvent> { _ ->
            if (onlySword && mc.player.heldItemMainhand.item !is ItemSword) {
                return@safeEventListener
            }

            val entity = mc.world.loadedEntityList
                .asSequence()
                .filterIsInstance<EntityLivingBase>()
                .filter { it.canBeAttacked }
                .filter { mc.player.getDistance(it) <= distance }
                .filter {
                    when (it.entityType.invoke()) {
                        EntityType.Player -> players
                        EntityType.Monster -> monsters
                        EntityType.Animal -> animals
                        EntityType.Entity -> entities
                        EntityType.Vehicle -> vehicles
                        EntityType.Boss -> boss
                        EntityType.Other -> other
                    }
                }
                .minByOrNull {
                    when (target) {
                        Target.Distance -> mc.player.getDistance(it)
                        Target.Health -> (it as EntityLiving).health
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