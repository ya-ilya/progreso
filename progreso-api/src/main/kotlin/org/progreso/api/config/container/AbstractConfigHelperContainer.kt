package org.progreso.api.config.container

import org.progreso.api.config.AbstractConfigHelper

interface AbstractConfigHelperContainer {
    val helpers: MutableMap<AbstractConfigHelper<*>, String>

    fun getHelperByName(name: String): AbstractConfigHelper<*> {
        return getHelperByNameOrNull(name)!!
    }

    fun getHelperByNameOrNull(name: String): AbstractConfigHelper<*>? {
        return helpers.keys.firstOrNull { it.name == name }
    }

    fun getHelperConfig(helper: AbstractConfigHelper<*>): String? {
        return helpers[helper]
    }

    fun setHelperConfig(helper: AbstractConfigHelper<*>, config: String) {
        helpers[helper] = config
    }
}