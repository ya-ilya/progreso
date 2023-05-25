package org.progreso.client.manager

import org.progreso.client.manager.managers.minecraft.CombatManager
import org.progreso.client.manager.managers.render.TextRenderManager

object Managers {
    init {
        CombatManager
        TextRenderManager
    }
}