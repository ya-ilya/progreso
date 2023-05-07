package org.progreso.api.command.container

import org.progreso.api.command.AbstractCommand
import kotlin.reflect.KClass

/**
 * Interface for command containers
 *
 * Current implementations: CommandManager
 * TODO implementations: AbstractPlugin
 */
interface CommandContainer {
    val commands: MutableList<AbstractCommand>

    fun add(command: AbstractCommand) {
        commands.add(command)
    }

    operator fun get(value: String): AbstractCommand {
        return commands.first { it.name == value }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : AbstractCommand> get(clazz: KClass<T>): T {
        return commands.first { it::class == clazz } as T
    }

    operator fun iterator(): Iterator<AbstractCommand> {
        return commands.iterator()
    }
}