package org.progreso.client.module.modules.render

import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object FullBright : Module("FullBright", Category.Render) {
    init {
        var oldGamma = 0.5

        onEnable {
            oldGamma = mc.options?.gamma?.value ?: 0.5
        }

        onDisable {
            mc.options.gamma.value = oldGamma
        }

        safeEventListener<TickEvent> { _ ->
            mc.options.gamma.value = 1.0
        }
    }
}