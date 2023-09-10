package org.progreso.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType

@AbstractCommand.Register("toggle")
object ToggleCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            argument("module", ModuleArgumentType()).executesSuccess { context ->
                ModuleArgumentType[context].toggle()
            }
        )
    }
}