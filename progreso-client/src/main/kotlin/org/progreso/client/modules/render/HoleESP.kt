package org.progreso.client.modules.render

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.util.render.*
import org.progreso.client.util.world.getBlocksInRadius
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList

@AbstractModule.AutoRegister
object HoleESP : AbstractModule() {
    private val DEFAULT_BOX = Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    private val HOLE_DIRECTIONS = Direction.entries.filter { it != Direction.UP }

    private val radius by setting("Radius", 5, 3..25)
    private val color by setting("Color", Color.RED)

    private val holes = CopyOnWriteArrayList<BlockPos>()

    init {
        safeEventListener<TickEvent> {
            holes.clear()

            for (pos in mc.world.getBlocksInRadius(radius, mc.player.blockPos)) {
                if (isHole(pos)) {
                    holes.add(pos)
                }
            }
        }

        safeEventListener<Render3DEvent> { event ->
            for (pos in holes) {
                render3D(event.matrices) {
                    withPosition(pos) {
                        withColor(color.copy(50)) {
                            drawSolidBox(DEFAULT_BOX)
                        }

                        withColor(color.copy(100)) {
                            drawOutlinedBox(DEFAULT_BOX)
                        }
                    }
                }
            }
        }
    }

    private fun isHole(pos: BlockPos): Boolean {
        if (!isAir(pos)) return false
        if (!HOLE_DIRECTIONS.map { pos.offset(it) }.all { isObsidianOrBedrock(it) }) return false
        if (!isAir(pos.offset(Direction.UP))) return false
        return true
    }

    private fun isAir(pos: BlockPos): Boolean {
        return mc.world.getBlockState(pos).block == Blocks.AIR
    }

    private fun isObsidianOrBedrock(pos: BlockPos): Boolean {
        return mc.world.getBlockState(pos).block.let { it == Blocks.OBSIDIAN || it == Blocks.BEDROCK }
    }
}