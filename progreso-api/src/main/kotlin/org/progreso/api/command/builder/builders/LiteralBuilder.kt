package org.progreso.api.command.builder.builders

import org.progreso.api.command.builder.AbstractBuilder

open class LiteralBuilder(name: String) : AbstractBuilder<LiteralBuilder>(name) {
    override fun toString(): String {
        return name
    }
}