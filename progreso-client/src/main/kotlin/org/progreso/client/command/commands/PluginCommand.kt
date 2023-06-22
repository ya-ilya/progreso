package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.PluginArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.command.Command
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen

object PluginCommand : Command("plugin") {
    init {
        literal("info") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                info("--------")
                infoLocalized("command.plugin.name_entry", "name" to plugin.name)
                infoLocalized("command.plugin.version_entry", "version" to plugin.version)
                infoLocalized("command.plugin.author_entry", "author" to plugin.author)
            }
        }

        literal("list").executes { _ ->
            infoLocalized(
                "command.plugin.list",
                "plugins" to PluginManager.plugins.joinToString { it.name }
            )
        }

        literal("gui").executes { _ ->
            mc.setScreen(ProgresoPluginsScreen(PluginManager.plugins))
        }
    }

    private fun CommandContext.plugin(): AbstractPlugin? {
        return nullable<AbstractPlugin>("plugin")
    }
}