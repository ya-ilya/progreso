package org.progreso.client.modules.render

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.util.level.getBlocksInRadius
import org.progreso.client.util.render.drawOutlinedBox
import org.progreso.client.util.render.drawSolidBox
import org.progreso.client.util.render.render3D
import org.progreso.client.util.render.withRelativeToCameraPosition
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList

@AbstractModule.AutoRegister
object HoleESP : AbstractModule() {
    private val DEFAULT_BOX = AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    private val HOLE_DIRECTIONS = Direction.entries.filter { it != Direction.UP }

    private val radius by setting("Radius", 5, 3..25)
    private val color by setting("Color", Color.RED)

    private val holes = CopyOnWriteArrayList<BlockPos>()

    init {
        safeEventListener<TickEvent> {
            holes.clear()

            for (pos in mc.level.getBlocksInRadius(radius, mc.player.blockPosition())) {
                if (isHole(pos)) {
                    holes.add(pos)
                }
            }
        }

        safeEventListener<Render3DEvent> { event ->
            render3D(event.matrices) {
                for (pos in holes) {
                    withRelativeToCameraPosition(pos) {
                        drawSolidBox(DEFAULT_BOX, color.copy(50))
                        drawOutlinedBox(DEFAULT_BOX, color.copy(100))
                    }
                }
            }
        }
    }

    private fun isHole(pos: BlockPos): Boolean {
        if (!isAir(pos)) return false
        if (!HOLE_DIRECTIONS.map { pos.offset(it.unitVec3i) }.all { isObsidianOrBedrock(it) }) return false
        if (!isAir(pos.offset(Direction.UP.unitVec3i))) return false
        return true
    }

    private fun isAir(pos: BlockPos): Boolean {
        return mc.level.getBlockState(pos).block == Blocks.AIR
    }

    private fun isObsidianOrBedrock(pos: BlockPos): Boolean {
        return mc.level.getBlockState(pos).block.let { it == Blocks.OBSIDIAN || it == Blocks.BEDROCK }
    }
}