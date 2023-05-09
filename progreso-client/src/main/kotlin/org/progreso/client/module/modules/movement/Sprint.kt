package org.progreso.client.module.modules.movement

import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module

class Sprint : Module("Sprint", Category.Movement) {
    init {
        safeEventListener<TickEvent> { _ ->
            if (mc.gameSettings.keyBindForward.isKeyDown) {
                mc.player.isSprinting = true
            }
        }
    }
}