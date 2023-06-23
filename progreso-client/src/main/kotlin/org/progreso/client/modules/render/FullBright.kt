package org.progreso.client.modules.render

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener

@AbstractModule.Register("FullBright", Category.Render)
object FullBright : AbstractModule() {
    init {
        var oldGamma = 0.5

        onEnable {
            oldGamma = mc.options?.gamma?.value ?: 0.5
        }

        onDisable {
            mc.options!!.gamma.value = oldGamma
        }

        safeEventListener<TickEvent> { _ ->
            mc.options!!.gamma.value = 1.0
        }
    }
}