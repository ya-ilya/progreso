package org.progreso.client.module.modules.combat

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.init.Items
import net.minecraft.item.Item
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.InventoryUtil

object Offhand : Module("Offhand", Category.Combat) {
    private val mode by setting("Mode", Mode.Totem)
    private val totemHealth by setting("TotemHealth", 10, 1..20)

    init {
        safeEventListener<TickEvent> { _ ->
            if (mc.player.isCreative && mc.currentScreen is GuiContainer) return@safeEventListener

            if (mc.player.health <= totemHealth) {
                Mode.Totem.switch()
            } else {
                mode.switch()
            }
        }
    }

    @Suppress("unused")
    private enum class Mode(val item: Item, val sortByMetadata: Boolean = false) {
        Totem(Items.TOTEM_OF_UNDYING),
        Crystal(Items.END_CRYSTAL),
        Gapple(Items.GOLDEN_APPLE, true);

        fun switch() {
            if (mc.player.heldItemOffhand.item == item) return
            var slots = InventoryUtil.getSlotsByItem(item)

            if (sortByMetadata) {
                slots = slots.sortedByDescending { it.stack.metadata }
            }

            val slot = slots.firstOrNull()

            if (slot != null) {
                InventoryUtil.task {
                    click(slot.slotNumber)
                    click(45)
                    click(slot.slotNumber)
                }
            }
        }
    }
}