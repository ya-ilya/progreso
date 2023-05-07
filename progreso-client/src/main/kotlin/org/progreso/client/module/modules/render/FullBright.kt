package org.progreso.client.module.modules.render

import org.progreso.client.events.misc.TickEvent
import org.progreso.client.module.Category
import org.progreso.client.module.Module

class FullBright : Module("FullBright", Category.Render) {
    init {
        var oldGamma = 0f

        onEnable {
            oldGamma = mc.gameSettings.gammaSetting
        }

        safeEventListener<TickEvent> { _ ->
            mc.gameSettings.gammaSetting = 1000.0f
        }

        onDisable {
            mc.gameSettings.gammaSetting = oldGamma
        }
    }
}