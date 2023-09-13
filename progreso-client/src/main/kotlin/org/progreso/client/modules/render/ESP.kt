package org.progreso.client.modules.render

import net.minecraft.entity.Entity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.passive.PassiveEntity
import net.minecraft.entity.passive.SnowGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.util.render.*
import java.awt.Color

@AbstractModule.AutoRegister
object ESP : AbstractModule() {
    private val players by setting("Players", true)
    private val monsters by setting("Monsters", false)
    private val animals by setting("Animals", false)
    private val self by setting("Self", false)

    private val colors = setting("colors")
    private val playersColor by colors.setting("Players", Color.WHITE)
    private val monstersColor by colors.setting("Monsters", Color.RED)
    private val animalsColor by colors.setting("Animals", Color.GREEN)

    private val renderMap = mutableMapOf<Entity, Color>()

    init {
        safeEventListener<TickEvent> {
            renderMap.clear()

            for (entity in mc.world.entities) {
                if (entity == mc.player && !self) continue

                val (render, color) = when (entity) {
                    is PlayerEntity -> players to playersColor
                    is Monster -> monsters to monstersColor
                    is PassiveEntity, is WaterCreatureEntity, is SnowGolemEntity, is IronGolemEntity -> animals to animalsColor
                    else -> continue
                }

                if (render) renderMap[entity] = color
            }
        }

        safeEventListener<Render3DEvent> { event ->
            for ((entity, color) in renderMap) {
                val pos = Vec3d(
                    MathHelper.lerp(event.tickDelta.toDouble(), entity.lastRenderX, entity.x) - entity.x,
                    MathHelper.lerp(event.tickDelta.toDouble(), entity.lastRenderY, entity.y) - entity.y,
                    MathHelper.lerp(event.tickDelta.toDouble(), entity.lastRenderZ, entity.z) - entity.z
                )

                render3D(event.matrices) {
                    withPosition(pos) {
                        withColor(color.copy(50)) {
                            drawSolidBox(entity.boundingBox)
                        }

                        withColor(color.copy(100)) {
                            drawOutlinedBox(entity.boundingBox)
                        }
                    }
                }
            }
        }
    }
}
