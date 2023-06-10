package org.progreso.client.module.modules.combat

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen
import net.minecraft.item.Item
import net.minecraft.item.Items
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import org.progreso.client.util.player.InventoryUtil

object Offhand : Module("Offhand", Category.Combat) {
    private val mode by setting("Mode", Mode.Totem)
    private val totemHealth by setting("TotemHealth", 10, 1..20)

    init {
        safeEventListener<TickEvent> { _ ->
            if (mc.currentScreen is CreativeInventoryScreen) return@safeEventListener

            if (mc.player!!.health <= totemHealth) {
                Mode.Totem.switch()
            } else {
                mode.switch()
            }
        }
    }

    private enum class Mode(val item: Item) {
        Totem(Items.TOTEM_OF_UNDYING),
        Crystal(Items.END_CRYSTAL),
        Gapple(Items.ENCHANTED_GOLDEN_APPLE);

        fun switch() {
            if (mc.player!!.offHandStack.item == item) return
            val slot = InventoryUtil.findItem(fromIndex = 9) { _, stack -> stack.item == item }

            if (slot != null) {
                InventoryUtil.moveItem(slot.index, 45)
            }
        }
    }
}