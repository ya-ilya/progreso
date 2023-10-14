package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.PluginArgumentType
import org.progreso.api.managers.PluginManager

@AbstractCommand.Register("plugin")
object PluginCommand : AbstractCommand() {
    init {
        builder.then(
            literal("info").then(
                argument("plugin", PluginArgumentType()).execute { context ->
                    val plugin = PluginArgumentType[context]

                    infoLocalized("command.plugin.header")
                    infoLocalized("command.plugin.name_entry", plugin.name)
                    infoLocalized("command.plugin.version_entry", plugin.version)
                    infoLocalized("command.plugin.author_entry", plugin.author)
                }
            )
        )

        builder.then(literal("list").execute {
            infoLocalized(
                PluginManager.plugins.ifEmpty("command.plugin.list", "command.plugin.list_empty"),
                PluginManager.plugins.joinToString { it.name }
            )
        })
    }
}