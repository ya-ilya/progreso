package org.progreso.api.managers

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.progreso.api.Api
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.container.CommandContainer

object CommandManager : CommandContainer {
    private const val PREFIX = "."
    private val DISPATCHER = CommandDispatcher<Any>()

    override val commands = mutableListOf<AbstractCommand>()

    override fun addCommand(command: AbstractCommand) {
        command.register(DISPATCHER)
        super.addCommand(command)
    }

    fun onChat(message: String): Boolean {
        if (message.startsWith(PREFIX)) {
            try {
                if (DISPATCHER.execute(message.removePrefix(PREFIX), Any()) != SINGLE_SUCCESS) {
                    Api.CHAT.send("§cInvalid syntax")
                }
            } catch (ex: CommandSyntaxException) {
                Api.CHAT.send("§cInvalid syntax (see error in logs)")
                ex.printStackTrace()
            } catch (ex: Exception) {
                Api.CHAT.send("§cCommand not found")
                ex.printStackTrace()
            }

            Api.CHAT.addToSentMessages(message)
            return true
        }

        return false
    }
}