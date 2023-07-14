package org.progreso.api.plugin.container

import org.progreso.api.plugin.AbstractPlugin
import kotlin.reflect.KClass

/**
 * Interface for plugin containers
 */
interface PluginContainer {
    val plugins: MutableSet<AbstractPlugin>

    fun addPlugin(plugin: AbstractPlugin) {
        if (getPluginByNameOrNull(plugin.name) != null) {
            throw RuntimeException("Plugin ${plugin.name} already loaded")
        }

        plugins.add(plugin)
    }

    fun removePlugin(plugin: AbstractPlugin) {
        plugins.remove(plugin)
    }

    fun getPluginByName(name: String): AbstractPlugin {
        return getPluginByNameOrNull(name)!!
    }

    fun getPluginByNameOrNull(name: String): AbstractPlugin? {
        return plugins.firstOrNull { it.name == name }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractPlugin> getPluginByClass(clazz: KClass<T>): T {
        return plugins.first { it::class == clazz } as T
    }
}