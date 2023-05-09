package org.progreso.api.managers

import org.progreso.api.command.AbstractCommand
import org.progreso.api.config.AbstractConfigHelper
import org.progreso.api.config.container.AbstractConfigHelperContainer
import org.progreso.api.module.AbstractModule
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.api.plugin.container.PluginContainer
import javax.naming.OperationNotSupportedException

object PluginManager : PluginContainer {
    override val plugins = mutableListOf<AbstractPlugin>()

    val modules = mutableListOf<AbstractModule>()
    val commands = mutableListOf<AbstractCommand>()

    val configContainer = object : AbstractConfigHelperContainer {
        override val helpers = mutableMapOf<AbstractConfigHelper<*>, String>()

        override fun getHelperByName(name: String): AbstractConfigHelper<*> {
            throw OperationNotSupportedException()
        }
    }
}