package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.modules.SimpleTextHudModule

@AbstractModule.AutoRegister
object Welcomer : SimpleTextHudModule({
    "Welcome to Progreso Client, ${mc.player.name.string}!"
})