package org.progreso.api.command

import org.progreso.api.Api
import org.progreso.api.command.builder.builders.LiteralBuilder

/**
 * Command abstract class
 */
abstract class AbstractCommand : LiteralBuilder("") {
    private val annotation = javaClass.getAnnotation(Register::class.java)

    override val name = annotation.name
    val description = annotation.description

    annotation class Register(
        val name: String,
        val description: String = ""
    )

    protected companion object {
        fun send(message: Any) = Api.CHAT.send(message)

        fun sendLocalized(key: String, vararg args: Any) =
            Api.CHAT.sendLocalized(key, *args)

        fun info(message: Any) = Api.CHAT.info(message)

        fun infoLocalized(key: String, vararg args: Any) =
            Api.CHAT.infoLocalized(key, *args)

        fun warn(message: Any) = Api.CHAT.warn(message)

        fun warnLocalized(key: String, vararg args: Any) =
            Api.CHAT.warnLocalized(key, *args)

        fun error(message: Any) = Api.CHAT.error(message)

        fun errorLocalized(key: String, vararg args: Any) =
            Api.CHAT.errorLocalized(key, *args)
    }
}