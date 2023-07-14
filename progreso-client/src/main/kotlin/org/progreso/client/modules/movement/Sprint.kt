package org.progreso.client.modules.movement

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener

@AbstractModule.AutoRegister
object Sprint : AbstractModule() {
    init {
        safeEventListener<TickEvent> { _ ->
            if (mc.options!!.forwardKey.isPressed) {
                mc.player.isSprinting = true
            }
        }
    }
}