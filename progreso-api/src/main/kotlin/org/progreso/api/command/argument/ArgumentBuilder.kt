package org.progreso.api.command.argument

import org.progreso.api.command.dispatcher.CommandContext

open class ArgumentBuilder {
    val nodes = mutableListOf<Node>()
    private var executes: (CommandContext) -> Unit = { }

    fun literal(
        name: String,
        block: ArgumentBuilder.() -> Unit = { }
    ): ArgumentBuilder {
        return ArgumentBuilder().apply(block).also {
            nodes.add(Node.LiteralNode(name, it))
        }
    }

    fun <T : Any?> argument(
        name: String,
        type: ArgumentType<T>,
        block: ArgumentBuilder.() -> Unit = { }
    ): ArgumentBuilder {
        return ArgumentBuilder().apply(block).also {
            nodes.add(Node.ArgumentNode(name, it, type))
        }
    }

    fun executes(block: (CommandContext) -> Unit): ArgumentBuilder {
        executes = block
        return this
    }

    fun findLiteral(predicate: (Node.LiteralNode) -> Boolean): Node.LiteralNode? {
        return nodes.filterIsInstance<Node.LiteralNode>().firstOrNull(predicate)
    }

    fun findArgument(predicate: (Node.ArgumentNode) -> Boolean): Node.ArgumentNode? {
        return nodes.filterIsInstance<Node.ArgumentNode>().firstOrNull(predicate)
    }

    operator fun invoke(context: CommandContext) {
        executes.invoke(context)
    }

    sealed class Node(val name: String, val builder: ArgumentBuilder) {
        class LiteralNode(name: String, builder: ArgumentBuilder) : Node(name, builder)
        class ArgumentNode(name: String, builder: ArgumentBuilder, val type: ArgumentType<*>) : Node(name, builder)
    }
}