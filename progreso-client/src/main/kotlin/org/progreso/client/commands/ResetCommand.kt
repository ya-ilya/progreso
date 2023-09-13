package org.progreso.client.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType
import org.progreso.api.command.arguments.SettingArgumentType

@AbstractCommand.Register("reset")
object ResetCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            argument("module", ModuleArgumentType()).then(
                argument("setting", SettingArgumentType()).executesSuccess { context ->
                    val (module, setting) = SettingArgumentType[context]

                    setting.reset()
                    infoLocalized(
                        "command.reset.setting",
                        setting.name,
                        module.name
                    )
                }
            )
        )
    }
}