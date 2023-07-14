package org.progreso.api.alt.container

import org.progreso.api.alt.AltAccount

/**
 * Interface for alt containers
 */
interface AltContainer {
    val alts: MutableSet<AltAccount>

    fun addAlt(alt: AltAccount) {
        alts.add(alt)
    }

    fun removeAlt(alt: AltAccount) {
        alts.remove(alt)
    }
}