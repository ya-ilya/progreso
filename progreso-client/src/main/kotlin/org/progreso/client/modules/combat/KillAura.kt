package org.progreso.client.modules.combat

import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.fish.WaterAnimal
import net.minecraft.world.entity.animal.golem.IronGolem
import net.minecraft.world.entity.animal.golem.SnowGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Items
import net.minecraft.world.item.MaceItem
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

    private val SWORDS = listOf(
        Items.WOODEN_SWORD,
        Items.STONE_SWORD,
        Items.IRON_SWORD,
        Items.GOLDEN_SWORD,
        Items.DIAMOND_SWORD,
        Items.NETHERITE_SWORD
    )

    init {
        safeEventListener<TickEvent> { _ ->
            when (mc.player.mainHandItem.item) {
                is AxeItem -> if (!axe) return@safeEventListener
                in SWORDS -> if (!sword) return@safeEventListener
                is MaceItem -> if (!mace) return@safeEventListener
                else -> return@safeEventListener
            }

            Items.WOODEN_SWORD

            val entity = mc.level.entitiesForRendering()
                .filterIsInstance<LivingEntity>()
                .filter { it.canBeAttacked }
                .filter { mc.player.distanceTo(it) <= distance }
                .filter {
                    when (it) {
                        is Player -> players
                        is Monster -> monsters
                        is AgeableMob, is WaterAnimal, is SnowGolem, is IronGolem -> animals
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
                mc.gameMode.attack(entity)
            }
        }
    }

    private enum class Target {
        Distance,
        Health
    }
}