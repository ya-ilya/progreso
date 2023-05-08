package org.progreso.api.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
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
    abstract fun build(builder: LiteralArgumentBuilder<Any>)

    fun register(dispatcher: CommandDispatcher<Any>) {
        dispatcher.register(literal(name).also { build(it) })
    }

    protected fun literal(name: String): LiteralArgumentBuilder<Any> {
        return LiteralArgumentBuilder.literal(name)!!
    }

    protected fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<Any, T> {
        return RequiredArgumentBuilder.argument(name, type)
    }

    protected fun send(message: String) = Api.CHAT.send(message)
}