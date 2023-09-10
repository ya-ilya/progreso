package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.arguments.ModuleArgumentType
import org.progreso.api.setting.settings.BindSetting
import org.progreso.client.util.client.KeyboardUtil

@AbstractCommand.Register("bind")
object BindCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            argument("module", ModuleArgumentType())
                .then(
                    argument("key", StringArgumentType.string()).executesSuccess { context ->
                        val module = ModuleArgumentType[context]
                        val key = StringArgumentType.getString(context, "key")

                        module.bind = try {
                            KeyboardUtil.getKeyCode(key)
                        } catch (ex: Exception) {
                            errorLocalized("command.bind.key_error", key)
                            return@executesSuccess
                        }

                        infoLocalized("command.bind.key", module.name, key)
                    }
                )
                .then(
                    literal("reset").executesSuccess { context ->
                        val module = ModuleArgumentType[context]
                        module.bind = module.getSettingByName("Bind", BindSetting::class).initialValue

                        infoLocalized("command.bind.reset", module.name)
                    }
                )
                .executesSuccess { context ->
                    val module = ModuleArgumentType[context]

                    infoLocalized("command.bind.current", module.name, KeyboardUtil.getKeyName(module.bind))
                }
        )
    }
}