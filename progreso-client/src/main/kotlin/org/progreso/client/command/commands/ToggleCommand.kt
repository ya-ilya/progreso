package org.progreso.client.command.commands

import org.progreso.api.command.argument.arguments.ModuleArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.module.AbstractModule
import org.progreso.client.command.Command

object ToggleCommand : Command("toggle") {
    init {
        argument("module", ModuleArgumentType.create()).executes { context ->
            val module = context.module() ?: return@executes
            module.toggle()
        }
    }

    private fun CommandContext.module(): AbstractModule? {
        return getNullable<AbstractModule>("module")
    }
}