package org.progreso.api.command.argument

import org.progreso.api.command.reader.StringReader

interface ArgumentType<T : Any?> {
    val name: String

    fun parse(reader: StringReader): T
    fun check(reader: StringReader): Boolean
}