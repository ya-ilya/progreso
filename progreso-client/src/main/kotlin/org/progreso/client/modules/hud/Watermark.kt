package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractModule
import org.progreso.client.modules.SimpleTextHudModule

@AbstractModule.AutoRegister
object Watermark : SimpleTextHudModule({
    "Progreso Client"
})