package org.progreso.api.command.dispatcher

import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.exceptions.SyntaxException
import org.progreso.api.command.reader.StringReader

class CommandDispatcher {
    private val nodes = mutableListOf<ArgumentBuilder.Node.LiteralNode>()

    fun register(name: String, builder: ArgumentBuilder) {
        nodes.add(ArgumentBuilder.Node.LiteralNode(name, builder))
    }

    fun unregister(name: String) {
        nodes.removeIf { it.name == name }
    }

    fun dispatch(string: String): Boolean {
        val (first, reader) = parseArguments(string)
        val node = nodes.firstOrNull { it.name == first } ?: return false
        executeBuilder(node.builder, reader, CommandContext())
        return true
    }

    fun predictNode(string: String): ArgumentBuilder.Node? {
        if (string.isBlank() || string.endsWith(" ")) return null
        val (first, reader) = parseArguments(string)
        val node = nodes.firstOrNull { it.name.startsWith(first) } ?: return null
        return predictNode(node, reader)
    }

    fun getVariants(string: String): List<String>? {
        if (string.isBlank() || !string.endsWith(" ")) return null
        val (first, reader) = parseArguments(string)
        val node = nodes.firstOrNull { it.name == first } ?: return null
        return getVariants(node, reader)
    }

    private fun parseArguments(string: String): Pair<String, StringReader> {
        val arguments = string.split(" ").let { arguments -> arguments.map { it.trim() } }
        return arguments[0] to StringReader(arguments.drop(1).joinToString(" "))
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

    private fun predictNode(
        node: ArgumentBuilder.Node,
        reader: StringReader
    ): ArgumentBuilder.Node? {
        if (!reader.hasNext()) {
            return if (node is ArgumentBuilder.Node.LiteralNode) {
                node
            } else {
                null
            }
        }

        val string = reader.peek()
        val literal = node.builder.findLiteral { it.name.startsWith(string) }

        return if (literal == null) {
            reader.readString()
            val argument = node.builder.findArgument { it.type.checkType(reader) } ?: return null
            predictNode(argument, reader)
        } else {
            reader.readString() // Skip literal string
            predictNode(literal, reader)
        }
    }

    private fun getVariants(
        node: ArgumentBuilder.Node,
        reader: StringReader
    ): List<String>? {
        if (!reader.hasNext()) {
            return node.builder.nodes.map {
                if (it is ArgumentBuilder.Node.ArgumentNode) {
                    "${it.name} (${it.type.name})"
                } else {
                    it.name
                }
            }
        }

        val string = reader.peek()
        val literal = node.builder.findLiteral { it.name.startsWith(string) }

        return if (literal == null) {
            reader.readString()
            val argument = node.builder.findArgument { it.type.checkType(reader) } ?: return null
            getVariants(argument, reader)
        } else {
            reader.readString()
            getVariants(literal, reader)
        }
    }
}