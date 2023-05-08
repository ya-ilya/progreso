package org.progreso.api.managers

import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.config.container.AbstractConfigHelperContainer
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import javax.naming.OperationNotSupportedException

object PluginManager {
    private val plugins = mutableListOf<AbstractPlugin>()

    val modules = mutableListOf<AbstractModule>()

    val configContainer = object : AbstractConfigHelperContainer {
        override val helpers = mutableMapOf<AbstractConfigHelper<*>, String>()

        override fun get(name: String): AbstractConfigHelper<*> {
            throw OperationNotSupportedException()
        }
    }

    fun add(plugin: AbstractPlugin) {
        plugins.add(plugin)
    }

    operator fun get(name: String): AbstractPlugin {
        return plugins.first { it.name == name }
    }

    operator fun iterator(): Iterator<AbstractPlugin> {
        return plugins.iterator()
    }
}