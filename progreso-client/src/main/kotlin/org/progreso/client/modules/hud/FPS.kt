package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.modules.SimpleTextHudModule

@AbstractModule.AutoRegister
object FPS : SimpleTextHudModule({
    "FPS: ${mc.client.currentFps}"
})