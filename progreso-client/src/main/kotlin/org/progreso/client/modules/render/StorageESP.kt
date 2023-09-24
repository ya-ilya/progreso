package org.progreso.client.modules.render

import net.minecraft.block.entity.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.render.Render3DEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.gui.clickgui.element.elements.ColorElement.Companion.copy
import org.progreso.client.modules.render.ESP.espSetting
import org.progreso.client.util.render.*
import org.progreso.client.util.world.blockEntities
import java.awt.Color

@AbstractModule.AutoRegister
object StorageESP : AbstractModule() {
    private val DEFAULT_BOX = Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)

    private val chest by espSetting("Chest", true, Color.ORANGE)
    private val enderChest by espSetting("EnderChest", true, Color.MAGENTA)
    private val furnace by espSetting("Furnace", true, Color.BLACK)
    private val dispenser by espSetting("Dispenser", true, Color.BLACK)
    private val hopper by espSetting("Hopper", true, Color.GRAY)
    private val shulkerBox by espSetting("ShulkerBox", true, Color(0x6e, 0x4d, 0x6e).brighter())

    private val renderMap = mutableMapOf<BlockPos, Color>()

    init {
        safeEventListener<TickEvent> {
            renderMap.clear()

            for (blockEntity in mc.world.blockEntities) {
                val (render, color) = when (blockEntity) {
                    is ChestBlockEntity -> chest
                    is EnderChestBlockEntity -> enderChest
                    is FurnaceBlockEntity -> furnace
                    is DispenserBlockEntity -> dispenser
                    is HopperBlockEntity -> hopper
                    is ShulkerBoxBlockEntity -> shulkerBox
                    else -> continue
                }

                if (render) renderMap[blockEntity.pos] = color
            }
        }

        safeEventListener<Render3DEvent> { event ->
            for ((pos, color) in renderMap) {
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
}