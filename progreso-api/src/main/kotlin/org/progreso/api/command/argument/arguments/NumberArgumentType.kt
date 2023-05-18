package org.progreso.api.command.argument.arguments

import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class NumberArgumentType<T : Number>(
    private val type: KClass<T>
) : ArgumentType<T> {
    companion object {
        inline fun <reified T : Number> number() = NumberArgumentType(T::class)
    }

    override val name = "number"

    override fun parse(reader: StringReader): T {
        val string = reader.readString()

        return when (type) {
            Short::class -> string.toShort()
            Int::class -> string.toInt()
            Long::class -> string.toLong()
            Float::class -> string.toFloat()
            Double::class -> string.toDouble()
            else -> throw RuntimeException()
        } as T
    }

    override fun checkType(reader: StringReader): Boolean {
        val peeked = reader.peek()

        return if (type == Float::class || type == Double::class) {
            peeked.count { it == '.' } <= 1 && peeked.all { it.isDigit() || it == '.' }
        } else {
            peeked.all { it.isDigit() }
        }
    }
}