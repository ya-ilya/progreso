package org.progreso.client.util.player

import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerInput
import net.minecraft.world.item.ItemStack
import org.progreso.client.Client.Companion.mc

data class Slot(val index: Int, val stack: ItemStack)

val Inventory.hotbar get() = findItems(0, 9) { _, _ -> true }

fun Inventory.findItemInHotbar(predicate: (Int, ItemStack) -> Boolean): Slot? {
    return findItem(0, 9, predicate)
}

fun Inventory.findItem(
    fromIndex: Int? = null,
    toIndex: Int? = null,
    predicate: (Int, ItemStack) -> Boolean
): Slot? {
    return findItems(fromIndex, toIndex, predicate).firstOrNull()
}

fun Inventory.findItems(
    fromIndex: Int? = null,
    toIndex: Int? = null,
    predicate: (Int, ItemStack) -> Boolean
): List<Slot> {
    val result = mutableListOf<Slot>()

    val end = toIndex ?: this.containerSize
    for (i in (fromIndex ?: 0) until end) {
        val itemStack = getItem(i)

        if (predicate(i, itemStack)) {
            result.add(Slot(i, itemStack))
        }
    }

    return result
}

fun Inventory.updateSelectedSlot(index: Int) {
    mc.connection!!.send(ServerboundSetCarriedItemPacket(index))

    this.selected = index
}

fun MultiPlayerGameMode.moveItem(fromIndex: Int, toIndex: Int) {
    val player = mc.player

    val syncId = player.containerMenu.containerId

    this.handleContainerInput(syncId, fromIndex, 0, ContainerInput.PICKUP, player)
    this.handleContainerInput(syncId, toIndex, 0, ContainerInput.PICKUP, player)
    this.handleContainerInput(syncId, fromIndex, 0, ContainerInput.PICKUP, player)
}