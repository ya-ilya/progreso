package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.commands.arguments.KeyArgumentType
import org.progreso.client.util.client.KeyboardUtil

@AbstractCommand.Register("bind")
object BindCommand : AbstractCommand() {
    init {
        builder.then(
            argument("module", ModuleArgumentType())
                .then(
                    argument("key", KeyArgumentType()).execute { context ->
                        val module = ModuleArgumentType[context]
                        val key = KeyArgumentType[context]

                        module.bind = key
                        infoLocalized("command.bind.key", module.name, key)
                    }
                )
                .then(
                    literal("reset").execute { context ->
                        val module = ModuleArgumentType[context]
                        module.bind = module.getSettingByName("Bind", BindSetting::class).initialValue

                        infoLocalized("command.bind.reset", module.name)
                    }
                )
                .execute { context ->
                    val module = ModuleArgumentType[context]

                    infoLocalized("command.bind.current", module.name, KeyboardUtil.getKeyName(module.bind))
                }
        )
    }
}