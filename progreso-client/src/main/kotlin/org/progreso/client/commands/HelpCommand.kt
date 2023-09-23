package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.managers.CommandManager

@AbstractCommand.Register("help")
object HelpCommand : AbstractCommand() {
    init {
        builder.execute {
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