package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.modules.SimpleHudModule

@AbstractModule.AutoRegister
object Welcomer : SimpleHudModule({
    "Welcome to Progreso Client, ${mc.player.name.string}!"
})