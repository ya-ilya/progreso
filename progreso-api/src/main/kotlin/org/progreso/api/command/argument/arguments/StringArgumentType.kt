package org.progreso.api.command.argument.arguments

import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader

class StringArgumentType(
    private val greedy: Boolean = false
) : ArgumentType<String> {
    companion object {
        fun string(greedy: Boolean = false) = StringArgumentType(greedy)
    }

    override val name = "string"

    override fun parse(reader: StringReader): String {
        return if (greedy) {
            val builder = StringBuilder()

            while (reader.hasNext()) {
                builder.append(reader.readString())
                if (reader.hasNext()) builder.append(" ")
            }

            builder.toString()
        } else {
            reader.readString()
        }
    }

    override fun checkType(reader: StringReader): Boolean {
        return true
    }
}