package org.progreso.api.module.container

import org.progreso.api.common.Container
import org.progreso.api.module.AbstractModule
import kotlin.reflect.KClass

/**
 * Interface for module containers
 */
interface ModuleContainer : Container {
    val modules: MutableSet<AbstractModule>

    fun addModule(module: AbstractModule) {
        modules.add(module)
    }

    fun removeModule(module: AbstractModule) {
        modules.remove(module)
    }

    fun getModuleByName(name: String): AbstractModule {
        return getModuleByNameOrNull(name)!!
    }

    fun getModuleByNameOrNull(name: String): AbstractModule? {
        return modules.firstOrNull { it.name == name }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractModule> getModuleByClass(clazz: KClass<T>): T {
        return modules.first { it::class == clazz } as T
    }
}