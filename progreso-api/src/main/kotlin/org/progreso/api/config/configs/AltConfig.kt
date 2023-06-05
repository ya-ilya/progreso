package org.progreso.api.config.configs

import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.config.AbstractConfig

class AltConfig(name: String, val alts: List<AbstractAltAccount>) : AbstractConfig(name)