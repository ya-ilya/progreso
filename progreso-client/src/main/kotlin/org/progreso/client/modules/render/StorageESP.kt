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
import org.progreso.client.util.render.*
import org.progreso.client.util.world.WorldUtil.blockEntities
import java.awt.Color

@AbstractModule.AutoRegister
object StorageESP : AbstractModule() {
    private val DEFAULT_BOX = Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)

    private val chest by setting("Chest", true)
    private val enderChest by setting("EnderChest", true)
    private val furnace by setting("Furnace", true)
    private val dispenser by setting("Dispenser", true)
    private val hopper by setting("Hopper", true)
    private val shulkerBox by setting("ShulkerBox", true)

    private val colors = setting("Colors")
    private val chestColor by colors.setting("Chest", Color.ORANGE)
    private val enderChestColor by colors.setting("EnderChest", Color.MAGENTA)
    private val furnaceColor by colors.setting("Furnace", Color.BLACK)
    private val dispenserColor by colors.setting("Dispenser", Color.BLACK)
    private val hopperColor by colors.setting("Hopper", Color.GRAY)
    private val shulkerBoxColor by colors.setting("ShulkerBox", Color(0x6e, 0x4d, 0x6e).brighter())

    private val renderMap = mutableMapOf<BlockPos, Color>()

    init {
        safeEventListener<TickEvent> {
            renderMap.clear()

            for (blockEntity in mc.world.blockEntities) {
                val (render, color) = when (blockEntity) {
                    is ChestBlockEntity -> chest to chestColor
                    is EnderChestBlockEntity -> enderChest to enderChestColor
                    is FurnaceBlockEntity -> furnace to furnaceColor
                    is DispenserBlockEntity -> dispenser to dispenserColor
                    is HopperBlockEntity -> hopper to hopperColor
                    is ShulkerBoxBlockEntity -> shulkerBox to shulkerBoxColor
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