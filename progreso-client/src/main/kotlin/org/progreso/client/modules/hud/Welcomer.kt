package org.progreso.client.modules.hud

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.modules.SimpleTextHudModule

@AbstractModule.AutoRegister
object Welcomer : SimpleTextHudModule({
    i18n("module.welcomer.text", mc.player.name.string)
})