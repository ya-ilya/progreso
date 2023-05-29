package org.progreso.api.plugin

import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer
import org.progreso.api.config.helpers.ModuleConfigHelper
import org.progreso.api.config.providers.ModuleConfigProvider
import org.progreso.api.event.events.PluginEvent
import org.progreso.api.managers.PluginManager
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.container.ModuleContainer
import kotlin.properties.Delegates

/**
 * Abstract plugin class
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractPlugin : ModuleContainer, CommandContainer {
    override val modules = mutableSetOf<AbstractModule>()
    override val commands = mutableSetOf<AbstractCommand>()

    var name: String by Delegates.notNull()
    var version: String by Delegates.notNull()
    var author: String by Delegates.notNull()

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

        Api.API_EVENT_BUS.post(PluginEvent(this, true))
    }

    fun unloadPlugin() {
        unload()

        configHelper.save(name)

        modules.forEach { it.enabled = false }
        PluginManager.modules.removeAll(modules)
        PluginManager.commands.removeAll(commands)

        Api.API_EVENT_BUS.post(PluginEvent(this, false))
    }
}