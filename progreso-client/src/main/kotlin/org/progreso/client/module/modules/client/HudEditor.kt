package org.progreso.client.module.modules.client

import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object HudEditor : Module("HudEditor", Category.Client) {
    init {
        onEnable {
            mc.setScreen(HudEditor)
            toggle()
        }
    }
}