package org.progreso.client.modules.movement

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.player.ClipAtLedgeEvent
import org.progreso.client.events.safeEventListener

@AbstractModule.AutoRegister
object SafeWalk : AbstractModule() {
    init {
        safeEventListener<ClipAtLedgeEvent> { event ->
            if (!mc.player.isSneaking) event.clip = true
        }
    }
}