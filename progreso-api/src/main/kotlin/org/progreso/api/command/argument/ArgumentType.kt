package org.progreso.api.command.argument

import org.progreso.api.command.reader.StringReader

interface ArgumentType<T : Any?> {
    fun parse(reader: StringReader): T

    fun checkType(reader: StringReader): Boolean
}