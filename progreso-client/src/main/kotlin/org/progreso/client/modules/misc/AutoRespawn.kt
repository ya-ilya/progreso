package org.progreso.client.modules.misc

import net.minecraft.client.gui.screen.DeathScreen
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.eventListener
import org.progreso.client.events.render.ScreenEvent

@AbstractModule.AutoRegister
object AutoRespawn : AbstractModule() {
    init {
        eventListener<ScreenEvent.Open> { event ->
            if (event.screen !is DeathScreen) return@eventListener
            mc.player.requestRespawn()
            event.cancel()
        }
    }
}