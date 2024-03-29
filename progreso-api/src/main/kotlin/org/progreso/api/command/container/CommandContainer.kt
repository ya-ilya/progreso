package org.progreso.api.command.container

import org.progreso.api.command.AbstractCommand
import org.progreso.api.common.Container

import kotlin.reflect.KClass

/**
 * Interface for command containers
 */
interface CommandContainer : Container {
    val commands: MutableSet<AbstractCommand>

    fun addCommand(command: AbstractCommand) {
        commands.add(command)
    }

    fun removeCommand(command: AbstractCommand) {
        commands.remove(command)
    }

    fun getCommandByName(name: String): AbstractCommand {
        return commands.first { it.name == name }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractCommand> getCommandByClass(clazz: KClass<T>): T {
        return commands.first { it::class == clazz } as T
    }
}