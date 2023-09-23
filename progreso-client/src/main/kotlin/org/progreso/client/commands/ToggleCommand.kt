package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType

@AbstractCommand.Register("toggle")
object ToggleCommand : AbstractCommand() {
    init {
        builder.then(
            argument("module", ModuleArgumentType()).execute { context ->
                ModuleArgumentType[context].toggle()
            }
        )
    }
}