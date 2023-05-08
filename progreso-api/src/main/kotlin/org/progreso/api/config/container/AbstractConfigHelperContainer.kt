package org.progreso.api.config.container

import org.progreso.api.config.AbstractConfigHelper

interface AbstractConfigHelperContainer {
    val helpers: MutableMap<AbstractConfigHelper<*>, String>

    operator fun get(name: String): AbstractConfigHelper<*> {
        return getOrNull(name)!!
    }

    fun getOrNull(name: String): AbstractConfigHelper<*>? {
        return helpers.keys.firstOrNull { it.name == name }
    }

    operator fun get(helper: AbstractConfigHelper<*>): String? {
        return helpers[helper]
    }

    operator fun set(helper: AbstractConfigHelper<*>, config: String) {
        helpers[helper] = config
    }
}