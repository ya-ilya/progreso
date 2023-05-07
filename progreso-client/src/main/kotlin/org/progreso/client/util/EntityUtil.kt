package org.progreso.client.util

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.*
import net.minecraft.entity.monster.*
import net.minecraft.entity.passive.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityEvokerFangs
import net.minecraft.entity.projectile.EntityFireball
import net.minecraft.entity.projectile.EntityShulkerBullet
import org.progreso.api.managers.FriendManager

object EntityUtil {
    private val mc: Minecraft = Minecraft.getMinecraft()

    val Entity.canBeAttacked: Boolean
        get() {
            if (this == mc.player) return false
            if (!isEntityAlive) return false
            if (!canBeAttackedWithItem()) return false
            return !(this is EntityPlayer && FriendManager.isFriend(name))
        }

    val Entity.entityType: () -> EntityType
        get() {
            when (this) {
                is EntityWolf -> {
                    return { if (isAngryWolf()) EntityType.Monster else EntityType.Animal }
                }

                is EntityEnderman -> {
                    return { if (isAngryEnderMan()) EntityType.Monster else EntityType.Entity }
                }

                is EntityPolarBear -> {
                    return { if (isAngryPolarBear()) EntityType.Monster else EntityType.Animal }
                }

                is EntityPigZombie -> {
                    return { if (isAngryPigMan()) EntityType.Monster else EntityType.Entity }
                }

                is EntityIronGolem -> {
                    return { if (isAngryGolem()) EntityType.Monster else EntityType.Entity }
                }

                is EntityVillager -> {
                    return { EntityType.Entity }
                }

                is EntityRabbit -> {
                    return { if (isFriendlyRabbit()) EntityType.Animal else EntityType.Monster }
                }
            }

            return when {
                isAnimal() -> {
                    { EntityType.Animal }
                }

                isMonster() -> {
                    { EntityType.Monster }
                }

                isPlayer() -> {
                    { EntityType.Player }
                }

                isVehicle() -> {
                    { EntityType.Vehicle }
                }

                isBoss() -> {
                    { EntityType.Boss }
                }

                isOther() -> {
                    { EntityType.Other }
                }

                else -> {
                    { EntityType.Entity }
                }
            }
        }

    private fun Entity.isPlayer(): Boolean {
        return this is EntityPlayer
    }

    private fun Entity.isAnimal(): Boolean {
        return (this is EntityPig
            || this is EntityParrot
            || this is EntityCow
            || this is EntitySheep
            || this is EntityChicken
            || this is EntitySquid
            || this is EntityBat
            || this is EntityVillager
            || this is EntityOcelot
            || this is EntityHorse
            || this is EntityLlama
            || this is EntityMule
            || this is EntityDonkey
            || this is EntitySkeletonHorse
            || this is EntityZombieHorse
            || this is EntitySnowman
            || (this is EntityRabbit
            && isFriendlyRabbit()))
    }

    private fun Entity.isMonster(): Boolean {
        return (this is EntityCreeper
            || this is EntityIllusionIllager
            || this is EntitySkeleton
            || (this is EntityZombie
            && this !is EntityPigZombie) || this is EntityBlaze
            || this is EntitySpider
            || this is EntityWitch
            || this is EntitySlime
            || this is EntitySilverfish
            || this is EntityGuardian
            || this is EntityEndermite
            || this is EntityGhast
            || this is EntityEvoker
            || this is EntityShulker
            || this is EntityWitherSkeleton
            || this is EntityStray
            || this is EntityVex
            || this is EntityVindicator
            || (this is EntityPolarBear
            && !isAngryPolarBear()) || (this is EntityWolf
            && !isAngryWolf()) || (this is EntityPigZombie
            && !isAngryPigMan()) || (this is EntityEnderman
            && !isAngryEnderMan()) || (this is EntityRabbit
            && !isFriendlyRabbit()) || (this is EntityIronGolem
            && !isAngryGolem()))
    }

    private fun Entity.isBoss(): Boolean {
        return (this is EntityDragon
            || this is EntityWither
            || this is EntityGiantZombie)
    }

    private fun Entity.isOther(): Boolean {
        return (this is EntityEnderCrystal
            || this is EntityEvokerFangs
            || this is EntityShulkerBullet
            || this is EntityFallingBlock
            || this is EntityFireball
            || this is EntityEnderEye
            || this is EntityEnderPearl)
    }

    private fun Entity.isVehicle(): Boolean {
        return this is EntityBoat || this is EntityMinecart
    }

    private fun EntityEnderman.isAngryEnderMan(): Boolean {
        return isScreaming
    }

    private fun EntityPigZombie.isAngryPigMan(): Boolean {
        return rotationPitch == 0.0f && revengeTimer <= 0
    }

    private fun EntityIronGolem.isAngryGolem(): Boolean {
        return rotationPitch == 0.0f
    }

    private fun EntityWolf.isAngryWolf(): Boolean {
        return isAngry
    }

    private fun EntityPolarBear.isAngryPolarBear(): Boolean {
        return rotationPitch == 0.0f && revengeTimer <= 0
    }

    private fun EntityRabbit.isFriendlyRabbit(): Boolean {
        return rabbitType != 99
    }

    enum class EntityType {
        Player,
        Monster,
        Animal,
        Entity,
        Vehicle,
        Boss,
        Other
    }
}