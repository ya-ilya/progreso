package org.progreso.api.plugin

import org.progreso.api.config.helpers.ModuleConfigHelper
import org.progreso.api.config.providers.ModuleConfigProvider
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
) : ModuleContainer {
    override val modules = mutableListOf<AbstractModule>()

    private val configHelper = ModuleConfigHelper(
        name = "plugin",
        path = "plugins",
        provider = ModuleConfigProvider(this),
        container = PluginManager.configContainer,
        defaultConfigName = name
    )

    init {
        PluginManager.configContainer[configHelper] = name
    }

    abstract fun initialize()
    abstract fun uninitialize()

    fun initializePlugin() {
        initialize()

        configHelper.load(name)

        for (module in modules) {
            PluginManager.modules.add(module)
        }
    }

    fun uninitializePlugin() {
        uninitialize()

        configHelper.save(name)

        for (module in modules) {
            PluginManager.modules.remove(module)
        }
    }
}