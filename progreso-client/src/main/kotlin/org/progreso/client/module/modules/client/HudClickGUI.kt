package org.progreso.client.module.modules.client

import org.progreso.client.Client
import org.progreso.client.module.Category
import org.progreso.client.module.Module

class HudClickGUI : Module("HudClickGUI", Category.Client) {
    init {
        onEnable {
            mc.displayGuiScreen(Client.HUD_EDITOR)
            toggle()
        }
    }
}