package org.progreso.api.command.argument.arguments

import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.reader.StringReader

class BooleanArgumentType : ArgumentType<Boolean> {
    companion object {
        fun boolean() = BooleanArgumentType()
    }

    override val name = "boolean"

    override fun parse(reader: StringReader): Boolean {
        return reader.readString().toBooleanStrict()
    }

    override fun checkType(reader: StringReader): Boolean {
        return reader.peek().toBooleanStrictOrNull() != null
    }
}