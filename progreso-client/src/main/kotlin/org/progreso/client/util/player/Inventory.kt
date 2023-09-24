package org.progreso.client.util.player

import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
import net.minecraft.screen.slot.SlotActionType
import org.progreso.client.Client.Companion.mc

data class Slot(val index: Int, val stack: ItemStack)

val PlayerInventory.hotbar get() = findItems(0, 9) { _, _ -> true }

fun PlayerInventory.findItemInHotbar(predicate: (Int, ItemStack) -> Boolean): Slot? {
    return findItem(0, 9, predicate)
}

fun PlayerInventory.findItem(
    fromIndex: Int? = null,
    toIndex: Int? = null,
    predicate: (Int, ItemStack) -> Boolean
): Slot? {
    return findItems(fromIndex, toIndex, predicate).firstOrNull()
}

fun PlayerInventory.findItems(
    fromIndex: Int? = null,
    toIndex: Int? = null,
    predicate: (Int, ItemStack) -> Boolean
): List<Slot> {
    val result = mutableListOf<Slot>()

    for (i in (fromIndex ?: 0)..<(toIndex ?: size())) {
        val itemStack = getStack(i)

        if (predicate(i, itemStack)) {
            result.add(Slot(i, itemStack))
        }
    }

    return result
}

fun PlayerInventory.updateSelectedSlot(index: Int) {
    mc.networkHandler.sendPacket(UpdateSelectedSlotC2SPacket(index))
    selectedSlot = index
}

fun ClientPlayerInteractionManager.moveItem(fromIndex: Int, toIndex: Int) {
    val syncId = mc.player.currentScreenHandler?.syncId ?: 0

    clickSlot(syncId, fromIndex, 0, SlotActionType.PICKUP, mc.player)
    clickSlot(syncId, toIndex, 0, SlotActionType.PICKUP, mc.player)
    clickSlot(syncId, fromIndex, 0, SlotActionType.PICKUP, mc.player)
}