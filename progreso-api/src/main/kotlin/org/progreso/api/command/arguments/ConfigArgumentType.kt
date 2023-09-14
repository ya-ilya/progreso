package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.config.AbstractConfig
import org.progreso.api.config.AbstractConfigCategory
import java.util.concurrent.CompletableFuture

class ConfigArgumentType(private val category: AbstractConfigCategory<*>) : ArgumentType<AbstractConfig> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        private val NO_SUCH_CONFIG = DynamicCommandExceptionType { name: Any ->
            val (categoryName, configName) = name as Pair<String, String>

            Api.TEXT.i18nMessage("argument.config.error", configName, categoryName)
        }

        operator fun get(context: CommandContext<*>): AbstractConfig {
            return context.getArgument("config", AbstractConfig::class.java)
        }
    }

    override fun parse(reader: StringReader): AbstractConfig {
        val argument = reader.readString()

        return category.configs.firstOrNull { it.name == argument }
            ?: throw NO_SUCH_CONFIG.create(category.name to argument)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(category.configs.map { it.name }, builder)
    }
}