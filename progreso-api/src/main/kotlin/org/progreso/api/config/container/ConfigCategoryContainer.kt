package org.progreso.api.config.container

import org.progreso.api.config.AbstractConfigCategory

interface ConfigCategoryContainer {
    val categories: MutableSet<AbstractConfigCategory<*>>

    fun getCategoryByName(name: String): AbstractConfigCategory<*> {
        return getCategoryByNameOrNull(name)!!
    }

    fun getCategoryByNameOrNull(name: String): AbstractConfigCategory<*>? {
        return categories.firstOrNull { it.name == name }
    }
}