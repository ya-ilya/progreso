package org.progreso.client.commands.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.client.accessors.TextAccessor
import org.progreso.client.managers.minecraft.ProgresoResourceManager
import java.util.concurrent.CompletableFuture

class FontArgumentType : ArgumentType<String> {
    companion object {
        private val NO_SUCH_FONT = DynamicCommandExceptionType { name: Any ->
            TextAccessor.i18nMessage("argument.font.error", name)
        }

        private val EXAMPLES: Collection<String> = listOf("arial", "roboto")

        operator fun get(context: CommandContext<*>): String {
            return StringArgumentType.getString(context, "font")
        }
    }

    override fun parse(reader: StringReader): String {
        val argument: String = reader.readString()

        return ProgresoResourceManager.fonts.firstOrNull { it == argument }
            ?: throw NO_SUCH_FONT.create(argument)
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(ProgresoResourceManager.fonts, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}