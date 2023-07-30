package org.progreso.api.command.dispatcher

import org.progreso.api.command.builder.AbstractBuilder
import org.progreso.api.command.builder.builders.ArgumentBuilder
import org.progreso.api.command.builder.builders.LiteralBuilder
import org.progreso.api.command.exceptions.SyntaxException
import org.progreso.api.command.reader.StringReader

class CommandDispatcher {
    private val builders = mutableListOf<LiteralBuilder>()

    fun register(builder: LiteralBuilder) {
        builders.add(builder)
    }

    fun unregister(builder: LiteralBuilder) {
        builders.remove(builder)
    }

    fun dispatch(string: String): Boolean {
        val (first, reader) = parse(string)
        val builder = builders.firstOrNull { it.name == first } ?: return false
        dispatch(builder, reader, CommandContext())
        return true
    }

    fun predict(string: String): LiteralBuilder? {
        if (string.isBlank() || string.endsWith(" ")) return null
        val (first, reader) = parse(string)
        val builder = builders.firstOrNull { it.name.startsWith(first) } ?: return null
        return predict(builder, reader)
    }

    fun variants(string: String): List<AbstractBuilder<*>>? {
        if (string.isBlank() || !string.endsWith(" ")) return null
        val (first, reader) = parse(string)
        val builder = builders.firstOrNull { it.name == first } ?: return null
        return variants(builder, reader)
    }

    private fun dispatch(builder: AbstractBuilder<*>, reader: StringReader, context: CommandContext) {
        if (!reader.hasNext()) {
            builder.invoke(context)
            return
        }

        val string = reader.peek()
        val nextBuilder = builder.findBuilder<LiteralBuilder> { it.name == string }
            ?: builder.findBuilder<ArgumentBuilder> { it.type.check(reader) }
            ?: throw SyntaxException("Literal and argument not found for '${string}'")

        when (nextBuilder) {
            is ArgumentBuilder -> context[nextBuilder.name] = nextBuilder.type.parse(reader)
            else -> reader.readString()
        }

        dispatch(nextBuilder, reader, context)
    }

    private fun predict(builder: AbstractBuilder<*>, reader: StringReader): LiteralBuilder? {
        if (!reader.hasNext()) {
            return try {
                builder as LiteralBuilder?
            } catch (ex: ClassCastException) {
                null
            }
        }

        val string = reader.peek()
        val nextBuilder = builder.findBuilder<LiteralBuilder> { it.name.startsWith(string) }
            ?: builder.findBuilder<ArgumentBuilder> { it.type.check(reader) }
            ?: return null

        reader.readString()
        return predict(nextBuilder, reader)
    }

    private fun variants(builder: AbstractBuilder<*>, reader: StringReader): List<AbstractBuilder<*>>? {
        if (!reader.hasNext()) {
            return builder.children
        }

        val string = reader.peek()
        val nextBuilder = builder.findBuilder<LiteralBuilder> { it.name.startsWith(string) }
            ?: builder.findBuilder<ArgumentBuilder> { it.type.check(reader) }
            ?: return null

        reader.readString()
        return variants(nextBuilder, reader)
    }

    private fun parse(string: String): Pair<String, StringReader> {
        val arguments = string.split(" ").let { arguments -> arguments.map { it.trim() } }
        return arguments[0] to StringReader(arguments.drop(1).joinToString(" "))
    }
}