package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType
import org.progreso.api.command.arguments.SettingArgumentType

@AbstractCommand.Register("reset")
object ResetCommand : AbstractCommand() {
    init {
        builder.then(
            argument("module", ModuleArgumentType()).then(
                argument("setting", SettingArgumentType()).execute { context ->
                    val module = ModuleArgumentType[context]
                    val (setting, path) = SettingArgumentType[context]

                    setting.reset()
                    infoLocalized(
                        "command.reset.setting",
                        path,
                        module.name
                    )
                }
            )
        )
    }
}