package org.progreso.api.managers

import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer
import org.progreso.api.command.dispatcher.CommandDispatcher
import org.progreso.api.command.exceptions.SyntaxException

object CommandManager : CommandContainer {
    const val PREFIX = '.'
    const val PREFIX_CODE = 46
    val DISPATCHER = CommandDispatcher()

    override val commands = mutableSetOf<AbstractCommand>()

    override fun addCommand(command: AbstractCommand) {
        DISPATCHER.register(command)
        super.addCommand(command)
    }

    fun removeCommand(command: AbstractCommand) {
        DISPATCHER.unregister(command)
        commands.remove(command)
    }

    fun onChat(message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            try {
                if (!DISPATCHER.dispatch(message.removePrefix(PREFIX.toString()))) {
                    Api.CHAT.errorLocalized("command.command_not_found")
                }
            } catch (ex: SyntaxException) {
                Api.CHAT.errorLocalized("command.invalid_syntax", "message" to ex.message!!)
                ex.printStackTrace()
            } catch (ex: Exception) {
                Api.CHAT.errorLocalized("command.failed_to_execute")
                ex.printStackTrace()
            }

            Api.CHAT.addToSentMessages(message)
            return true
        }

        return false
    }
}