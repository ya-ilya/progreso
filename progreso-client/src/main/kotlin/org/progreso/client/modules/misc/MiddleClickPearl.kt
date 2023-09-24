package org.progreso.client.modules.misc

import net.minecraft.item.Items
import net.minecraft.util.Hand
import org.lwjgl.glfw.GLFW
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.input.MouseEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.util.player.findItemInHotbar

@AbstractModule.AutoRegister
object MiddleClickPearl : AbstractModule() {
    init {
        safeEventListener<MouseEvent> { event ->
            if (event.action != GLFW.GLFW_PRESS || event.button != GLFW.GLFW_MOUSE_BUTTON_MIDDLE || mc.currentScreen != null)
                return@safeEventListener

            val previousSelectedSlot = mc.player.inventory.selectedSlot
            val enderPearl = mc.player.inventory
                .findItemInHotbar { _, stack -> stack.item == Items.ENDER_PEARL }
                ?: return@safeEventListener

            mc.player.inventory.selectedSlot = enderPearl.index
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND)
            mc.player.inventory.selectedSlot = previousSelectedSlot
        }
    }
}