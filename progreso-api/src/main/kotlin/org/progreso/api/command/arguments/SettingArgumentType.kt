package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.container.SettingContainer
import java.util.concurrent.CompletableFuture

class SettingArgumentType : ArgumentType<String> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        private val NO_SUCH_SETTING = DynamicCommandExceptionType { name: Any ->
            val (moduleName, settingName) = name as Pair<String, String>

            Api.TEXT.i18nMessage("argument.setting.error", settingName, moduleName)
        }

        private fun getSettingPaths(container: SettingContainer): List<String> {
            val paths = mutableListOf<String>()

            for (setting in container.settings) {
                if (setting is SettingContainer) {
                    for (subPath in getSettingPaths(setting)) {
                        paths.add("${setting.name}.${subPath}")
                    }
                } else {
                    paths.add(setting.name)
                }
            }

            return paths
        }

        private fun getSettingByPath(path: String, container: SettingContainer): AbstractSetting<*>? {
            var currentContainer = container
            var currentSetting: AbstractSetting<*>? = null

            for (pathPart in path.split(".")) {
                val setting = currentContainer.getSettingByNameOrNull(pathPart, AbstractSetting::class)

                if (setting is SettingContainer) {
                    currentContainer = setting
                } else {
                    currentSetting = setting
                }
            }

            return currentSetting
        }

        operator fun get(context: CommandContext<*>): Pair<AbstractSetting<*>, String> {
            val path = StringArgumentType.getString(context, "setting")
            val module = ModuleArgumentType[context]

            return (getSettingByPath(path, module) ?: throw NO_SUCH_SETTING.create(module.name to path)) to path
        }
    }

    override fun parse(reader: StringReader): String {
        return reader.readString()
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(
            try { getSettingPaths(ModuleArgumentType[context]) } catch (ex: Exception) { emptyList() },
            builder
        )
    }
}