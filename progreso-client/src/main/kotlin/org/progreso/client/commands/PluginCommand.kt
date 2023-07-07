package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.PluginArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen

@AbstractCommand.Register("plugin")
object PluginCommand : AbstractCommand() {
    init {
        literal("info") {
            argument("plugin", PluginArgumentType.create()).executes { context ->
                val plugin = context.plugin() ?: return@executes

                info("--------")
                infoLocalized("command.plugin.name_entry", plugin.name)
                infoLocalized("command.plugin.version_entry", plugin.version)
                infoLocalized("command.plugin.author_entry", plugin.author)
            }
        }

        literal("list").executes { _ ->
            infoLocalized(
                "command.plugin.list",
                PluginManager.plugins.joinToString { it.name }
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