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
import org.progreso.api.setting.container.SettingContainer
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.util.render.*
import java.awt.Color
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@AbstractModule.AutoRegister
object ESP : AbstractModule() {
    private val players by espSetting("Players", true, Color.WHITE)
    private val monsters by espSetting("Monsters", false, Color.RED)
    private val animals by espSetting("Animals", false, Color.GREEN)
    private val self by setting("Self", false)

    private val renderMap = ConcurrentHashMap<Entity, Color>()

    fun SettingContainer.espSetting(
        name: String,
        render: Boolean,
        color: Color
    ): ReadWriteProperty<Any?, Pair<Boolean, Color>> {
        return object : ReadWriteProperty<Any?, Pair<Boolean, Color>> {
            private val groupSetting = setting(name)
            private var renderSetting by groupSetting.setting("Render", render)
            private var colorSetting by groupSetting.setting("Color", color)

            override fun getValue(thisRef: Any?, property: KProperty<*>): Pair<Boolean, Color> {
                return renderSetting to colorSetting
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: Pair<Boolean, Color>) {
                renderSetting = value.first
                colorSetting = value.second
            }
        }
    }

    init {
        safeEventListener<TickEvent> {
            renderMap.clear()

            for (entity in mc.world.entities) {
                if (entity == mc.player && !self) continue

                val (render, color) = when (entity) {
                    is PlayerEntity -> players
                    is Monster -> monsters
                    is PassiveEntity, is WaterCreatureEntity, is SnowGolemEntity, is IronGolemEntity -> animals
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
                val box = entity.boundingBox.expand(0.1, 0.0, 0.1)

                render3D(event.matrices) {
                    withRelativeToCameraPosition(pos) {
                        withColor(color.copy(50)) {
                            drawSolidBox(box)
                        }

                        withColor(color.copy(100)) {
                            drawOutlinedBox(box)
                        }
                    }
                }
            }
        }
    }
}
