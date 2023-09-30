package org.progreso.api.plugin

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer
import org.progreso.api.config.categories.ModuleConfigCategory
import org.progreso.api.managers.PluginManager
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.container.ModuleContainer

/**
 * Abstract plugin class
 */
abstract class AbstractPlugin : ModuleContainer, CommandContainer {
    override val modules = mutableSetOf<AbstractModule>()
    override val commands = mutableSetOf<AbstractCommand>()

    lateinit var name: String
    lateinit var version: String
    lateinit var author: String

    private val configCategory by lazy {
        ModuleConfigCategory(
            name = "plugin",
            path = "plugins",
            container = this,
            defaultConfigName = name
        )
    }

    abstract fun load()
    abstract fun unload()

    fun loadPlugin() {
        load()

        configCategory.load(name)

        PluginManager.modules.addAll(modules)
        PluginManager.commands.addAll(commands)
    }

    fun unloadPlugin() {
        unload()

        configCategory.save(name)

        modules.forEach { it.enabled = false }
        PluginManager.modules.removeAll(modules)
        PluginManager.commands.removeAll(commands)
    }
}