package org.progreso.client.command.commands

import com.mojang.realmsclient.gui.ChatFormatting
import org.progreso.api.command.argument.arguments.PluginArgumentType
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.argument.ArgumentBuilder
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
                    plugin.initializePlugin()
                    send("Loaded ${plugin.name} plugin")
                } catch (ex: Exception) {
                    send("${ChatFormatting.RED}Error loading plugin (see error in logs)")
                    ex.printStackTrace()
                }
            }
        }
        
        builder.literal("unload") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                plugin.uninitializePlugin()
                PluginManager.plugins.remove(plugin)

                send("Unloaded ${plugin.name} plugin")
            }
        }
        
        builder.literal("info") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                send("--------")
                send("Name: ${plugin.name}")
                send("Version: ${plugin.version}")
                send("Author: ${plugin.author}")

                if (plugin.description != null) {
                    send("Description: ${plugin.description}")
                }
            }
        }
        
        builder.literal("list").executes { _ ->
            send("Plugins: ${PluginManager.plugins.joinToString { it.name }}")
        }
    }
    
    private fun CommandContext.plugin(): AbstractPlugin? {
        return getNullable<AbstractPlugin>("plugin")
    }
}