package org.progreso.api.managers

import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer

object CommandManager : CommandContainer {
    const val PREFIX = "."

    override val commands = mutableListOf<AbstractCommand>()

    fun onChat(message: String): Boolean {
        val args = message.split(" ")

        if (args[0].startsWith(PREFIX)) {
            val commandName = args[0].removePrefix(PREFIX)
            commands.firstOrNull { it.name == commandName }?.execute(args.drop(1))
                ?: Api.CHAT.send("§cCommand not found.")

            Api.CHAT.addToSentMessages(message)
            return true
        }

        return false
    }
}