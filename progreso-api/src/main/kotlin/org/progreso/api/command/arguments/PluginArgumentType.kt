package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.managers.PluginManager
import org.progreso.api.plugin.AbstractPlugin
import java.util.concurrent.CompletableFuture

class PluginArgumentType : ArgumentType<AbstractPlugin> {
    companion object {
        private val NO_SUCH_PLUGIN = DynamicCommandExceptionType { name: Any ->
            Api.TEXT.i18nMessage("argument.plugin.error", name)
        }

        operator fun get(context: CommandContext<*>): AbstractPlugin {
            return context.getArgument("plugin", AbstractPlugin::class.java)
        }
    }

    override fun parse(reader: StringReader): AbstractPlugin {
        val pluginName = reader.readString()
        return PluginManager.getPluginByNameOrNull(pluginName) ?: throw NO_SUCH_PLUGIN.create(pluginName)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(PluginManager.plugins.map { it.name }, builder)
    }
}