package org.progreso.api.module.container

import org.progreso.api.module.AbstractModule
import kotlin.reflect.KClass

/**
 * Interface for module containers
 */
interface ModuleContainer {
    val modules: MutableSet<AbstractModule>

    fun addModule(module: AbstractModule) {
        modules.add(module)
    }

    fun getModuleByName(name: String): AbstractModule {
        return modules.first { it.name == name }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractModule> getModuleByClass(clazz: KClass<T>): T {
        return modules.first { it::class == clazz } as T
    }
}