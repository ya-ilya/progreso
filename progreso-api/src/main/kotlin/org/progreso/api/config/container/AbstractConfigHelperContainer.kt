package org.progreso.api.config.container

import org.progreso.api.config.AbstractConfigHelper

interface AbstractConfigHelperContainer {
    val helpers: MutableMap<AbstractConfigHelper<*>, String>

    operator fun get(name: String): AbstractConfigHelper<*> {
        return helpers.keys.first { it.name == name }
    }

    operator fun get(helper: AbstractConfigHelper<*>): String? {
        return helpers[helper]
    }

    operator fun set(helper: AbstractConfigHelper<*>, config: String) {
        helpers[helper] = config
    }
}