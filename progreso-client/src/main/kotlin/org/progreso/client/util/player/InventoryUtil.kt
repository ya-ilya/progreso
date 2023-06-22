package org.progreso.client.util.player

import net.minecraft.item.ItemStack
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
import net.minecraft.screen.slot.SlotActionType
import org.progreso.client.Client.Companion.mc

object InventoryUtil {
    data class Slot(val index: Int, val stack: ItemStack)

    val hotbar get() = findItems(0, 9) { _, _ -> true }

    fun findItemInHotbar(predicate: (Int, ItemStack) -> Boolean): Slot? {
        return findItem(0, 9, predicate)
    }

    fun findItem(
        fromIndex: Int? = null,
        toIndex: Int? = null,
        predicate: (Int, ItemStack) -> Boolean
    ): Slot? {
        return findItems(fromIndex, toIndex, predicate).firstOrNull()
    }

    fun findItems(
        fromIndex: Int? = null,
        toIndex: Int? = null,
        predicate: (Int, ItemStack) -> Boolean
    ): List<Slot> {
        val result = mutableListOf<Slot>()

        for (i in (fromIndex ?: 0) until (toIndex ?: mc.player.inventory.size())) {
            val itemStack = mc.player.inventory.getStack(i)

            if (predicate(i, itemStack)) {
                result.add(Slot(i, itemStack))
            }
        }

        return result
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        val syncId = mc.player.currentScreenHandler?.syncId ?: 0

        mc.interactionManager.clickSlot(syncId, fromIndex, 0, SlotActionType.PICKUP, mc.player)
        mc.interactionManager.clickSlot(syncId, toIndex, 0, SlotActionType.PICKUP, mc.player)
        mc.interactionManager.clickSlot(syncId, fromIndex, 0, SlotActionType.PICKUP, mc.player)
    }

    fun updateSelectedSlot(index: Int) {
        mc.networkHandler.sendPacket(UpdateSelectedSlotC2SPacket(index))
        mc.player.inventory.selectedSlot = index
    }
}