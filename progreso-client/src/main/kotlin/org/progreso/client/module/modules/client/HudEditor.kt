package org.progreso.client.module.modules.client

import org.progreso.api.managers.AltManager
import org.progreso.client.gui.minecraft.ProgresoAltsScreen
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object HudEditor : Module("HudEditor", Category.Client) {
    init {
        onEnable {
            mc.setScreen(ProgresoAltsScreen(AltManager.alts))
            toggle()
        }
    }
}