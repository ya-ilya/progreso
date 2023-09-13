package org.progreso.api.managers

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
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
        val literal = LiteralArgumentBuilder.literal<Any>(command.name)
        command.build(literal)
        DISPATCHER.register(literal)
        super.addCommand(command)
    }

    fun dispatch(input: String) {
        try {
            if (DISPATCHER.execute(input.removePrefix(PREFIX), SOURCE) != SINGLE_SUCCESS) {
                Api.CHAT.errorLocalized("command.command_not_found")
            }
        } catch (ex: CommandSyntaxException) {
            Api.CHAT.errorLocalized("command.invalid_syntax", ex.message!!)
            ex.printStackTrace()
        } catch (ex: Exception) {
            Api.CHAT.errorLocalized("command.failed_to_execute")
            ex.printStackTrace()
        }
    }

    fun onChat(message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            dispatch(message)
            Api.CHAT.addToSentMessages(message)
            return true
        }

        return false
    }
}