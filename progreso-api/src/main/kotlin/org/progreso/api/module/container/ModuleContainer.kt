package org.progreso.api.module.container

import org.progreso.api.module.AbstractModule
import kotlin.reflect.KClass

/**
 * Interface for module containers
 *
 * Current implementations: ModuleManager
 * TODO implementations: AbstractPlugin
 */
interface ModuleContainer {
    val modules: MutableList<AbstractModule>

    fun add(module: AbstractModule) {
        modules.add(module)
    }

    operator fun get(value: String): AbstractModule {
        return modules.first { it.name == value }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : AbstractModule> get(clazz: KClass<T>): T {
        return modules.first { it::class == clazz } as T
    }

    operator fun iterator(): Iterator<AbstractModule> {
        return modules.iterator()
    }
}