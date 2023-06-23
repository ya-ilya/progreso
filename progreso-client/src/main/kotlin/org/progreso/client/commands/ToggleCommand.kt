package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.ModuleArgumentType
import org.progreso.api.command.dispatcher.CommandContext
import org.progreso.api.module.AbstractModule

@AbstractCommand.Register("toggle")
object ToggleCommand : AbstractCommand() {
    init {
        argument("module", ModuleArgumentType.create()).executes { context ->
            val module = context.module() ?: return@executes
            module.toggle()
        }
    }

    private fun CommandContext.module(): AbstractModule? {
        return nullable<AbstractModule>("module")
    }
}