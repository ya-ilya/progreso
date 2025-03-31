package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.client.Client.Companion.mc

@AbstractCommand.Register("clear")
object ClearCommand : AbstractCommand() {
    init {
        builder.execute {
            mc.inGameHud.chatHud.clear(true)
        }
    }
}