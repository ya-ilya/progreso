package org.progreso.client.modules.render

import net.minecraft.util.Mth
import net.minecraft.world.entity.AgeableMob
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.animal.fish.WaterAnimal
import net.minecraft.world.entity.animal.golem.IronGolem
import net.minecraft.world.entity.animal.golem.SnowGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.container.SettingContainer
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.util.render.drawOutlinedBox
import org.progreso.client.util.render.drawSolidBox
import org.progreso.client.util.render.render3D
import org.progreso.client.util.render.withRelativeToCameraPosition
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

            for (entity in mc.level.entitiesForRendering()) {
                if (entity == mc.player && !self) continue

                val (render, color) = when (entity) {
                    is Player -> players
                    is Monster -> monsters
                    is AgeableMob, is WaterAnimal, is SnowGolem, is IronGolem -> animals
                    else -> continue
                }

                if (render) renderMap[entity] = color
            }
        }

        safeEventListener<Render3DEvent> { event ->
            render3D(event.matrices) {
                for ((entity, color) in renderMap) {
                    val interpolatedX = Mth.lerp(event.tickDelta.toDouble(), entity.xo, entity.x)
                    val interpolatedY = Mth.lerp(event.tickDelta.toDouble(), entity.yo, entity.y)
                    val interpolatedZ = Mth.lerp(event.tickDelta.toDouble(), entity.zo, entity.z)

                    val pos = Vec3(
                        interpolatedX - entity.x,
                        interpolatedY - entity.y,
                        interpolatedZ - entity.z
                    )

                    val box = entity.boundingBox
                        .inflate(0.2)

                    withRelativeToCameraPosition(pos) {
                        drawSolidBox(box, color.copy(50))
                        drawOutlinedBox(box, color.copy(100))
                    }
                }
            }
        }
    }
}
