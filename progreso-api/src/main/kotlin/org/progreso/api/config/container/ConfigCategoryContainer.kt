package org.progreso.api.config.container

import org.progreso.api.config.AbstractConfigCategory

interface ConfigCategoryContainer {
    val categories: MutableMap<AbstractConfigCategory<*>, String>

    fun getCategoryByName(name: String): AbstractConfigCategory<*> {
        return getCategoryByNameOrNull(name)!!
    }

    fun getCategoryByNameOrNull(name: String): AbstractConfigCategory<*>? {
        return categories.keys.firstOrNull { it.name == name }
    }

    fun getCategoryConfig(category: AbstractConfigCategory<*>): String? {
        return categories[category]
    }

    fun setCategoryConfig(category: AbstractConfigCategory<*>, config: String) {
        categories[category] = config
    }
}