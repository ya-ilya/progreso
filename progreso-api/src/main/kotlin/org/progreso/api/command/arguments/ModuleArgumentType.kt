package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.container.ModuleContainer
import java.util.concurrent.CompletableFuture

class ModuleArgumentType(private val container: ModuleContainer = ModuleManager) : ArgumentType<AbstractModule> {
    companion object {
        private val NO_SUCH_MODULE = DynamicCommandExceptionType { name: Any ->
            Api.TEXT.i18nMessage("argument.module.error", name)
        }

        operator fun get(context: CommandContext<*>): AbstractModule {
            return context.getArgument("module", AbstractModule::class.java)
        }
    }

    private val examples = container.modules
        .take(3)
        .map { it.name }

    override fun parse(reader: StringReader): AbstractModule {
        val argument = reader.readString()

        return container.getModuleByNameOrNull(argument)
            ?: throw NO_SUCH_MODULE.create(argument)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(container.modules.map { it.name }, builder)
    }

    override fun getExamples(): Collection<String> {
        return examples
    }
}