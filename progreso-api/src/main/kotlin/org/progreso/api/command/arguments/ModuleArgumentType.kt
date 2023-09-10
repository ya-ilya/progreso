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
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

class ModuleArgumentType : ArgumentType<AbstractModule> {
    companion object {
        private val NO_SUCH_MODULE = DynamicCommandExceptionType { name: Any ->
            Api.TEXT.i18nMessage("argument.module.error", name)
        }

        private val EXAMPLES = ModuleManager.modules
            .stream()
            .limit(3)
            .map { it.name }
            .collect(Collectors.toList())

        operator fun get(context: CommandContext<*>): AbstractModule {
            return context.getArgument("module", AbstractModule::class.java)
        }
    }

    override fun parse(reader: StringReader): AbstractModule {
        val moduleName = reader.readString()
        return ModuleManager.getModuleByNameOrNull(moduleName) ?: throw NO_SUCH_MODULE.create(moduleName)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(ModuleManager.modules.map { it.name }, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}