package org.progreso.api.managers

import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.container.CommandContainer
import org.progreso.api.command.dispatcher.CommandDispatcher
import org.progreso.api.command.exceptions.SyntaxException

object CommandManager : CommandContainer {
    const val PREFIX = '.'
    val DISPATCHER = CommandDispatcher()

    override val commands = mutableSetOf<AbstractCommand>()

    override fun addCommand(command: AbstractCommand) {
        DISPATCHER.register(command.name, command)
        super.addCommand(command)
    }

    fun removeCommand(command: AbstractCommand) {
        DISPATCHER.unregister(command.name)
        commands.remove(command)
    }

    fun onChat(message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            try {
                if (!DISPATCHER.dispatch(message.removePrefix(PREFIX.toString()))) {
                    Api.CHAT.error("Command not found")
                }
            } catch (ex: SyntaxException) {
                Api.CHAT.error("Invalid syntax. ${ex.message}")
                ex.printStackTrace()
            } catch (ex: Exception) {
                Api.CHAT.error("Failed to execute command")
                ex.printStackTrace()
            }

            Api.CHAT.addToSentMessages(message)
            return true
        }

        return false
    }
}