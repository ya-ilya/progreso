package org.progreso.api.command

import org.progreso.api.Api
import org.progreso.api.command.argument.ArgumentBuilder

/**
 * Command abstract class
 *
 * @param name Command name
 * @param description Command description
 */
abstract class AbstractCommand(
    val name: String,
    val description: String
) {
    abstract fun build(builder: ArgumentBuilder)

    companion object {
        fun send(message: String) = Api.CHAT.send(message)
    }
}