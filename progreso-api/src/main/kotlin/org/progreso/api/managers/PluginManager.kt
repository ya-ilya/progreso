package org.progreso.api.managers

import org.progreso.api.command.AbstractCommand
import org.progreso.api.common.ObservableCollection
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.api.plugin.container.PluginContainer

object PluginManager : PluginContainer {
    override val plugins = mutableSetOf<AbstractPlugin>()

    val modules = mutableSetOf<AbstractModule>()
    val commands = object : ObservableCollection.Set<AbstractCommand>() {
        override fun elementAdded(element: AbstractCommand) {
            CommandManager.addCommand(element)
        }

        override fun elementRemoved(element: AbstractCommand) {
            CommandManager.removeCommand(element)
        }
    }
}