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

/**
 * Abstract plugin class
 *
 * @param name Plugin Name
 * @param version Plugin Version
 * @param author Plugin Author
 * @param description Plugin Description
 * @param mixinConfigs Plugin Mixin Configs
 */
@Suppress("MemberVisibilityCanBePrivate", "SpellCheckingInspection")
abstract class AbstractPlugin(
    val name: String,
    val version: String,
    val author: String,
    val description: String? = null,
    val mixinConfigs: Array<String> = arrayOf()
) : ModuleContainer, CommandContainer {
    override val modules = mutableListOf<AbstractModule>()
    override val commands = mutableListOf<AbstractCommand>()

    private val configHelper = ModuleConfigHelper(
        name = "plugin",
        path = "plugins",
        provider = ModuleConfigProvider(this),
        container = PluginManager.configContainer,
        defaultConfigName = name
    )

    init {
        PluginManager.configContainer.setHelperConfig(configHelper, name)
    }

    abstract fun initialize()
    abstract fun uninitialize()

    fun initializePlugin() {
        initialize()

        configHelper.load(name)

        PluginManager.modules.addAll(modules)
        PluginManager.commands.addAll(commands)

        Api.API_EVENT_BUS.post(PluginEvent(this, true))
    }

    fun uninitializePlugin() {
        uninitialize()

        configHelper.save(name)

        modules.forEach { it.enabled = false }
        PluginManager.modules.removeAll(modules)
        PluginManager.commands.removeAll(commands)

        Api.API_EVENT_BUS.post(PluginEvent(this, false))
    }
}