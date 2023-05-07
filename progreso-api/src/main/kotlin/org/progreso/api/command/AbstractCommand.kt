package org.progreso.api.command

import org.progreso.api.Api

/**
 * Command abstract class
 *
 * @param name Command name
 * @param description Command description
 */
abstract class AbstractCommand(
    val name: String,
    val description: String = "None"
) {
    abstract fun execute(args: List<String>)

    fun send(message: String) = Api.CHAT.send(message)
}