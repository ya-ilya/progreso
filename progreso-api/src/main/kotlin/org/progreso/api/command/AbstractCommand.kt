package org.progreso.api.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.progreso.api.Api


/**
 * Command abstract class
 */
abstract class AbstractCommand {
    private val annotation = javaClass.getAnnotation(Register::class.java)

    val name = annotation.name
    val description = annotation.description

    annotation class Register(
        val name: String,
        val description: String = ""
    )

    abstract fun build(builder: LiteralArgumentBuilder<Any>)

    protected companion object {
        fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<Any, T> {
            return RequiredArgumentBuilder.argument(name, type)
        }

        fun literal(name: String): LiteralArgumentBuilder<Any> {
            return LiteralArgumentBuilder.literal(name)
        }

        fun <S, T : ArgumentBuilder<S, T>> T.executesSuccess(block: (CommandContext<S>) -> Unit): T {
            return executes { context ->
                block(context)
                Command.SINGLE_SUCCESS
            }
        }

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