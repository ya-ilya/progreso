package org.progreso.client.modules.client

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.HudEditor

@AbstractModule.Register("HudEditor", Category.Client)
object HudEditor : AbstractModule() {
    init {
        onEnable {
            mc.setScreen(HudEditor)
            toggle()
        }
    }
}