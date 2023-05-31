package org.progreso.api.command

import org.progreso.api.Api
import org.progreso.api.command.builder.builders.LiteralBuilder

/**
 * Command abstract class
 *
 * @param name Command name
 * @param description Command description
 */
abstract class AbstractCommand(
    name: String,
    val description: String
) : LiteralBuilder(name) {
    protected companion object {
        fun send(message: Any) = Api.CHAT.send(message)
        fun info(message: Any) = Api.CHAT.info(message)
        fun warn(message: Any) = Api.CHAT.warn(message)
        fun error(message: Any) = Api.CHAT.error(message)
    }
}