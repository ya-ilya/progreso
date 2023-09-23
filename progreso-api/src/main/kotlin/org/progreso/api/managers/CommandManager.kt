package org.progreso.api.managers

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer

object CommandManager : CommandContainer {
    const val PREFIX = "."

    @JvmField
    var SOURCE = Api.COMMAND.createCommandSource()

    @JvmField
    val DISPATCHER = CommandDispatcher<Any>()

    override val commands = mutableSetOf<AbstractCommand>()

    override fun addCommand(command: AbstractCommand) {
        DISPATCHER.register(command.builder)
        super.addCommand(command)
    }

    fun dispatch(input: String) {
        try {
            DISPATCHER.execute(input.removePrefix(PREFIX), SOURCE)
        } catch (ex: CommandSyntaxException) {
            if (ex.cursor == 0) {
                Api.CHAT.errorLocalized("command.command_not_found")
            } else {
                Api.CHAT.errorLocalized("command.invalid_syntax", ex.message!!)
            }

            ex.printStackTrace()
        } catch (ex: Exception) {
            Api.CHAT.errorLocalized("command.failed_to_execute")
            ex.printStackTrace()
        }
    }
}