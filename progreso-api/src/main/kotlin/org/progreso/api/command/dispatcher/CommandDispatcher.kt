package org.progreso.api.command.dispatcher

import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.exceptions.SyntaxException
import org.progreso.api.command.reader.StringReader

class CommandDispatcher {
    private val builders = mutableMapOf<String, ArgumentBuilder>()

    fun register(name: String, builder: ArgumentBuilder) {
        builders[name] = builder
    }

    fun unregister(name: String) {
        builders.remove(name)
    }

    fun dispatch(string: String): Boolean {
        val arguments = string.split(" ").let { argument -> argument.map { it.trim() } }
        val builder = builders[arguments[0]] ?: return false
        val reader = StringReader(arguments.drop(1).joinToString(" "))
        executeBuilder(builder, reader, CommandContext())
        return true
    }

    private fun executeBuilder(
        builder: ArgumentBuilder,
        reader: StringReader,
        context: CommandContext
    ) {
        if (!reader.hasNext()) {
            builder.executes.invoke(context)
            return
        }

        val string = reader.peek()
        val literal = builder.findLiteral { it.name == string }

        if (literal == null) {
            val argument = builder.findArgument { it.type.checkType(reader) }
                ?: throw SyntaxException("Literal and argument not found for '${string}'")
            context.putArgument(argument.name, argument.type.parse(reader))
            executeBuilder(argument.builder, reader, context)
        } else {
            reader.readString() // Skip literal name
            executeBuilder(literal.builder, reader, context)
        }
    }
}