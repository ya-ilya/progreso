package org.progreso.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.managers.CommandManager

@AbstractCommand.Register("help")
object HelpCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.executesSuccess {
            infoLocalized("command.help.header")

            for (command in CommandManager.commands) {
                infoLocalized(
                    "command.help.entry",
                    command.name,
                    command.description
                )
            }
        }
    }
}