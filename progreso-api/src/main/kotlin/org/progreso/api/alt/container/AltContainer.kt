package org.progreso.api.alt.container

import org.progreso.api.alt.AltAccount
import org.progreso.api.common.Container

/**
 * Interface for alt containers
 */
interface AltContainer : Container {
    val alts: MutableSet<AltAccount>

    fun getAltByName(name: String): AltAccount {
        return getAltByNameOrNull(name)!!
    }

    fun getAltByNameOrNull(name: String): AltAccount? {
        return alts.firstOrNull { it.username == name }
    }

    fun addAlt(alt: AltAccount) {
        alts.add(alt)
    }

    fun removeAlt(alt: AltAccount) {
        alts.remove(alt)
    }
}