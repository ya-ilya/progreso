package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.PluginArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.command.Command
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen

object PluginCommand : Command("plugin") {
    init {
        literal("unload") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                plugin.unloadPlugin()
                PluginManager.plugins.remove(plugin)

                info("Unloaded ${plugin.name} plugin")
            }
        }

        literal("info") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                info("--------")
                info("Name: ${plugin.name}")
                info("Version: ${plugin.version}")
                info("Author: ${plugin.author}")
            }
        }

        literal("list").executes { _ ->
            info("Plugins: ${PluginManager.plugins.joinToString { it.name }}")
        }

        literal("gui").executes { _ ->
            mc.setScreen(ProgresoPluginsScreen(PluginManager.plugins.toList()))
        }
    }

    private fun CommandContext.plugin(): AbstractPlugin? {
        return getNullable<AbstractPlugin>("plugin")
    }
}