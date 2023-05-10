package org.progreso.client.command.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.realmsclient.gui.ChatFormatting
import org.progreso.api.command.arguments.PluginArgumentType
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.loader.PluginLoader
import org.progreso.client.command.Command

class PluginCommand : Command("plugin") {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(literal("load").then(
            argument("path", StringArgumentType.string()).executesSuccess { context ->
                val path = StringArgumentType.getString(context, "path")

                try {
                    val plugin = PluginLoader.loadPlugin(path)
                    PluginManager.addPlugin(plugin)
                    plugin.initializePlugin()
                    send("Loaded ${plugin.name} plugin")
                } catch (ex: Exception) {
                    send("${ChatFormatting.RED}Error loading plugin (see error in logs)")
                    ex.printStackTrace()
                }

                return@executesSuccess
            }
        ))

        builder.then(literal("unload").then(
            argument("plugin", PluginArgumentType.create()).executesSuccess { context ->
                val plugin = PluginArgumentType.get(context) ?: return@executesSuccess

                plugin.uninitializePlugin()
                PluginManager.plugins.remove(plugin)

                send("Unloaded ${plugin.name} plugin")

                return@executesSuccess
            }
        ))

        builder.then(literal("info").then(
            argument("plugin", PluginArgumentType.create()).executesSuccess { context ->
                val plugin = PluginArgumentType.get(context) ?: return@executesSuccess

                send("--------")
                send("Name: ${plugin.name}")
                send("Version: ${plugin.version}")
                send("Author: ${plugin.author}")

                if (plugin.description != null) {
                    send("Description: ${plugin.description}")
                }

                return@executesSuccess
            }
        ))

        builder.then(literal("list").executesSuccess { _ ->
            send("Plugins: ${PluginManager.plugins.joinToString { it.name }}")

            return@executesSuccess
        })
    }
}