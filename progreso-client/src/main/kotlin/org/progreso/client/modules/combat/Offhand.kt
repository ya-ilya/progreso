package org.progreso.client.modules.combat

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.util.player.findItem
import org.progreso.client.util.player.moveItem

@AbstractModule.AutoRegister
object Offhand : AbstractModule() {
    private val mode by setting("Mode", Mode.Totem)
    private val totemHealth by setting("TotemHealth", 10, 1..20)

    init {
        safeEventListener<TickEvent> { _ ->
            if (mc.screen is CreativeModeInventoryScreen) return@safeEventListener

            if (mc.player.health <= totemHealth) {
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
            if (mc.player.offhandItem.item == item) return
            val slot = mc.player.inventory.findItem(fromIndex = 9) { _, stack -> stack.item == item }

            if (slot != null) {
                mc.gameMode.moveItem(slot.index, 45)
            }
        }
    }
}