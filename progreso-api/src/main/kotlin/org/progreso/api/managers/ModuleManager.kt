package org.progreso.api.managers

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.api.module.container.ModuleContainer

object ModuleManager : ModuleContainer {
    override val modules = mutableSetOf<AbstractModule>()

    fun getModulesByCategory(category: Category): List<AbstractModule> {
        return (modules + PluginManager.modules).filter { it.category == category }
    }
}