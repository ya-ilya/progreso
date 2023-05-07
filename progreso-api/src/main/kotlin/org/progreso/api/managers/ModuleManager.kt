package org.progreso.api.managers

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.api.module.container.ModuleContainer

object ModuleManager : ModuleContainer {
    override val modules = mutableListOf<AbstractModule>()

    fun onKey(key: Int) {
        modules.filter { it.bind.value == key }.forEach { it.toggle() }
    }

    operator fun get(category: Category): List<AbstractModule> {
        return modules.filter { it.category == category }
    }
}