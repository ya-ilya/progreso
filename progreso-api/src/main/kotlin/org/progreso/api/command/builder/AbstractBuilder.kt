package org.progreso.api.command.builder

import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.builder.builders.ArgumentBuilder
import org.progreso.api.command.builder.builders.LiteralBuilder
import org.progreso.api.command.dispatcher.CommandContext

abstract class AbstractBuilder<S : AbstractBuilder<S>>(open val name: String) {
    val children = mutableListOf<AbstractBuilder<*>>()
    private var executes = { _: CommandContext -> }

    fun <T> argument(
        name: String,
        type: ArgumentType<T>,
        block: ArgumentBuilder.() -> Unit = { }
    ): ArgumentBuilder {
        return ArgumentBuilder(name, type).apply(block).also { children.add(it) }
    }

    fun literal(
        name: String,
        block: LiteralBuilder.() -> Unit = { }
    ): LiteralBuilder {
        return LiteralBuilder(name).apply(block).also { children.add(it) }
    }

    fun executes(block: (CommandContext) -> Unit): AbstractBuilder<S> {
        executes = block
        return this
    }

    inline fun <reified T : AbstractBuilder<*>> findBuilder(predicate: (T) -> Boolean): T? {
        return children.filterIsInstance<T>().firstOrNull(predicate)
    }

    operator fun invoke(context: CommandContext) {
        executes.invoke(context)
    }
}