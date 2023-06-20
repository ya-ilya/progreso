package org.progreso.api.alt.container

import org.progreso.api.alt.AbstractAltAccount

/**
 * Interface for alt containers
 */
interface AltContainer {
    val alts: MutableSet<AbstractAltAccount>

    fun addAlt(alt: AbstractAltAccount) {
        alts.add(alt)
    }

    fun removeAlt(alt: AbstractAltAccount) {
        alts.remove(alt)
    }
}