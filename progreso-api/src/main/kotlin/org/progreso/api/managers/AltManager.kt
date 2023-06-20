package org.progreso.api.managers

import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.alt.container.AltContainer

object AltManager : AltContainer {
    override val alts = mutableSetOf<AbstractAltAccount>()
}