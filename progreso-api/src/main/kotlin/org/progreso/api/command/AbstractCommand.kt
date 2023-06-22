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

        fun sendLocalized(key: String, vararg replacements: Pair<String, Any>) =
            Api.CHAT.sendLocalized(key, *replacements)

        fun info(message: Any) = Api.CHAT.info(message)

        fun infoLocalized(key: String, vararg replacements: Pair<String, Any>) =
            Api.CHAT.infoLocalized(key, *replacements)

        fun warn(message: Any) = Api.CHAT.warn(message)

        fun warnLocalized(key: String, vararg replacements: Pair<String, Any>) =
            Api.CHAT.warnLocalized(key, *replacements)

        fun error(message: Any) = Api.CHAT.error(message)

        fun errorLocalized(key: String, vararg replacements: Pair<String, Any>) =
            Api.CHAT.errorLocalized(key, *replacements)
    }
}