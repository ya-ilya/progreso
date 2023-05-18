package org.progreso.client.command.commands

import com.mojang.realmsclient.gui.ChatFormatting
import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.argument.arguments.PluginArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.api.plugin.loader.PluginLoader
import org.progreso.client.command.Command

class PluginCommand : Command("plugin") {
    override fun build(builder: ArgumentBuilder) {
        builder.literal("load") {
            argument("path", string()).executes { context ->
                val path = context.get<String>("path")

                try {
                    val plugin = PluginLoader.loadPlugin(path)
                    PluginManager.addPlugin(plugin)
                    plugin.loadPlugin()
                    info("Loaded ${plugin.name} plugin")
                } catch (ex: Exception) {
                    error("${ChatFormatting.RED}Error loading plugin (see error in logs)")
                    ex.printStackTrace()
                }
            }
        }

        builder.literal("unload") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                plugin.unloadPlugin()
                PluginManager.plugins.remove(plugin)

                info("Unloaded ${plugin.name} plugin")
            }
        }

        builder.literal("info") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                info("--------")
                info("Name: ${plugin.name}")
                info("Version: ${plugin.version}")
                info("Author: ${plugin.author}")

                if (plugin.description != null) {
                    info("Description: ${plugin.description}")
                }
            }
        }

        builder.literal("list").executes { _ ->
            info("Plugins: ${PluginManager.plugins.joinToString { it.name }}")
        }
    }

    private fun CommandContext.plugin(): AbstractPlugin? {
        return getNullable<AbstractPlugin>("plugin")
    }
}