package org.progreso.api.config.configs

import org.progreso.api.alt.AltAccount
import org.progreso.api.config.AbstractConfig

class AltConfig(name: String, val alts: List<AltAccount>) : AbstractConfig(name)