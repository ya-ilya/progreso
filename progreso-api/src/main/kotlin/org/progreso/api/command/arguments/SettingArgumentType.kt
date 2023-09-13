package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import org.progreso.api.Api
import org.progreso.api.module.AbstractModule
import org.progreso.api.setting.AbstractSetting

class SettingArgumentType : ArgumentType<String> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        private val NO_SUCH_SETTING = DynamicCommandExceptionType { name: Any ->
            val (moduleName, settingName) = name as Pair<String, String>

            Api.TEXT.i18nMessage("argument.setting.error", settingName, moduleName)
        }

        operator fun get(context: CommandContext<*>): Pair<AbstractModule, AbstractSetting<*>> {
            val settingName = StringArgumentType.getString(context, "setting")
            val module = ModuleArgumentType[context]

            return module to (
                module.getSettingByNameOrNull(settingName, AbstractSetting::class)
                    ?: throw NO_SUCH_SETTING.create(module.name to settingName)
            )
        }
    }

    override fun parse(reader: StringReader): String {
        return reader.readString()
    }
}