package org.progreso.api.config.providers

import org.progreso.api.config.AbstractConfigProvider
import org.progreso.api.config.configs.AltConfig
import org.progreso.api.managers.AltManager

object AltConfigProvider : AbstractConfigProvider<AltConfig>() {
    override fun create(name: String): AltConfig {
        return AltConfig(name, AltManager.alts.toList())
    }

    override fun apply(config: AltConfig) {
        AltManager.alts.clear()
        AltManager.alts.addAll(config.alts)
    }
}