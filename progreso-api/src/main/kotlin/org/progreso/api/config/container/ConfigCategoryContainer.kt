package org.progreso.api.config.container

import org.progreso.api.common.Container
import org.progreso.api.config.AbstractConfigCategory

/**
 * Interface for config category containers
 */
interface ConfigCategoryContainer : Container {
    val categories: MutableSet<AbstractConfigCategory<*, *>>

    fun getCategoryByName(name: String): AbstractConfigCategory<*, *> {
        return getCategoryByNameOrNull(name)!!
    }

    fun getCategoryByNameOrNull(name: String): AbstractConfigCategory<*, *>? {
        return categories.firstOrNull { it.name == name }
    }
}