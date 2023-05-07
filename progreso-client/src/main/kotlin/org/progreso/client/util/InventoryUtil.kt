package org.progreso.client.util

import net.minecraft.client.Minecraft
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.Slot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.concurrent.locks.ReentrantLock

object InventoryUtil {
    private val mc: Minecraft = Minecraft.getMinecraft()

    private val LOCK = ReentrantLock()

    val hotbar get() = mc.player.inventoryContainer.inventorySlots.filter { it.slotNumber in 36..44 }
    val currentItemStack: ItemStack get() = getSlotById(36 + mc.player.inventory.currentItem)!!.stack

    var currentItemIndex
        get() = mc.player.inventory.currentItem
        set(value) {
            mc.player.inventory.currentItem = value
        }

    fun task(block: InventoryUtil.() -> Unit) {
        try {
            LOCK.lock()
            this.block()
        } catch (ex: Exception) {
            LOCK.unlock()
        }
    }

    fun getSlotsByItem(item: Item): List<Slot> {
        return mc.player.inventoryContainer.inventorySlots
            .filter { it.stack.item == item }
    }

    fun getSlotById(slotId: Int): Slot? {
        return mc.player.inventoryContainer.inventorySlots
            .firstOrNull { it.slotNumber == slotId }
    }

    fun click(slotId: Int) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slotId, 0, ClickType.PICKUP, mc.player)
        mc.playerController.updateController()
    }
}