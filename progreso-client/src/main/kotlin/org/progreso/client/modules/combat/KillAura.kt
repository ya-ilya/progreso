package org.progreso.client.modules.combat

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.SnowGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.AxeItem
import net.minecraft.item.MaceItem
import net.minecraft.item.SwordItem
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.util.entity.canBeAttacked
import org.progreso.client.util.player.attack

@AbstractModule.AutoRegister
object KillAura : AbstractModule() {
    private val distance by setting("Distance", 4, 1..6)
    private val target by setting("Target", Target.Distance)

    private val weapons = setting("Weapons")
    private val axe by weapons.setting("Axe", false)
    private val sword by weapons.setting("Sword", true)
    private val mace by weapons.setting("Mace", false)

    private val targets = setting("Targets")
    private val players by targets.setting("Players", true)
    private val monsters by targets.setting("Monsters", false)
    private val animals by targets.setting("Animals", false)

    init {
        safeEventListener<TickEvent> { _ ->
            when (mc.player.mainHandStack.item) {
                is AxeItem -> if (!axe) return@safeEventListener
                is SwordItem -> if (!sword) return@safeEventListener
                is MaceItem -> if (!mace) return@safeEventListener
                else -> return@safeEventListener
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
                mc.interactionManager.attack(entity)
            }
        }
    }

    private enum class Target {
        Distance,
        Health
    }
}