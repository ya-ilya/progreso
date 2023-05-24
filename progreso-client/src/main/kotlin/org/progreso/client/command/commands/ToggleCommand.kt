package org.progreso.client.command.commands

import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.command.argument.arguments.ModuleArgumentType
import org.progreso.api.module.AbstractModule
import org.progreso.client.command.Command

class ToggleCommand : Command("toggle") {
    override fun build(builder: ArgumentBuilder) {
        builder.argument("module", ModuleArgumentType.create()).executes {
            val module = it.getNullable<AbstractModule>("module") ?: return@executes
            module.toggle()
        }
    }
}