package org.progreso.api.managers

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.api.module.container.ModuleContainer

object ModuleManager : ModuleContainer {
    override val modules = mutableSetOf<AbstractModule>()

    fun getModulesByCategory(category: Category, vararg exclude: AbstractModule): List<AbstractModule> {
        return (modules + PluginManager.modules)
            .filter { it.category == category }
            .filter { it !in exclude }
    }

    fun onKey(key: Int) {
        (modules + PluginManager.modules)
            .filter { it.bind == key }
            .forEach { it.toggle() }
    }
}