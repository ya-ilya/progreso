package org.progreso.api.managers

import org.progreso.api.command.AbstractCommand
import org.progreso.api.common.ObservableSet
import org.progreso.api.config.AbstractConfigCategory
import org.progreso.api.config.container.ConfigCategoryContainer
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.api.plugin.container.PluginContainer

object PluginManager : PluginContainer {
    override val plugins = mutableSetOf<AbstractPlugin>()

    val modules = mutableSetOf<AbstractModule>()
    val commands = object : ObservableSet<AbstractCommand>() {
        override fun elementAdded(element: AbstractCommand) {
            CommandManager.addCommand(element)
        }

        override fun elementRemoved(element: AbstractCommand) {
            CommandManager.removeCommand(element)
        }
    }

    val configContainer = object : ConfigCategoryContainer {
        override val categories = mutableMapOf<AbstractConfigCategory<*>, String>()

        override fun getCategoryByName(name: String): AbstractConfigCategory<*> {
            throw UnsupportedOperationException()
        }
    }
}