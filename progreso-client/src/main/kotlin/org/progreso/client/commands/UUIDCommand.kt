package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.client.commands.arguments.PlayerArgumentType

@AbstractCommand.Register("uuid")
object UUIDCommand : AbstractCommand() {
    init {
        builder.then(
            argument("player", PlayerArgumentType()).execute { context ->
                infoLocalized(
                    "command.uuid.player",
                    PlayerArgumentType[context].profile.name,
                    PlayerArgumentType[context].profile.id.toString()
                )
            }
        )
    }
}