package org.progreso.api.command.builder.builders

import org.progreso.api.command.argument.ArgumentType
import org.progreso.api.command.builder.AbstractBuilder

open class ArgumentBuilder(name: String, var type: ArgumentType<*>) : AbstractBuilder<ArgumentBuilder>(name) {
    override fun toString(): String {
        return "$name (${type.name})"
    }
}