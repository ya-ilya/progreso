package org.progreso.api.plugin

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer
import org.progreso.api.config.helpers.ModuleConfigHelper
import org.progreso.api.config.providers.ModuleConfigProvider
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

    private val configHelper by lazy {
        ModuleConfigHelper(
            name = "plugin",
            path = "plugins",
            provider = ModuleConfigProvider(this),
            container = PluginManager.configContainer,
            defaultConfigName = name
        )
    }

    abstract fun load()
    abstract fun unload()

    fun loadPlugin() {
        PluginManager.configContainer.setHelperConfig(configHelper, name)

        load()

        configHelper.load(name)

        PluginManager.modules.addAll(modules)
        PluginManager.commands.addAll(commands)
    }

    fun unloadPlugin() {
        unload()

        configHelper.save(name)

        modules.forEach { it.enabled = false }
        PluginManager.modules.removeAll(modules)
        PluginManager.commands.removeAll(commands)
    }
}