package org.progreso.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.PluginArgumentType
import org.progreso.api.managers.PluginManager
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen

@AbstractCommand.Register("plugin")
object PluginCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            literal("info").then(
                argument("plugin", PluginArgumentType()).executesSuccess { context ->
                    val plugin = PluginArgumentType[context]

                    info("--------")
                    infoLocalized("command.plugin.name_entry", plugin.name)
                    infoLocalized("command.plugin.version_entry", plugin.version)
                    infoLocalized("command.plugin.author_entry", plugin.author)
                }
            )
        )

        builder.then(literal("list").executesSuccess {
            infoLocalized(
                PluginManager.plugins.ifEmpty("command.plugin.list", "command.plugin.list_empty"),
                PluginManager.plugins.joinToString { it.name }
            )
        })

        builder.then(literal("gui").executesSuccess {
            mc.setScreen(ProgresoPluginsScreen(PluginManager.plugins))
        })
    }
}