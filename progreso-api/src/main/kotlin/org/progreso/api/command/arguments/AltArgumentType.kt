package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.alt.AltAccount
import org.progreso.api.alt.container.AltContainer
import org.progreso.api.managers.AltManager
import java.util.concurrent.CompletableFuture

class AltArgumentType(private val container: AltContainer = AltManager) : ArgumentType<AltAccount> {
    companion object {
        private val NO_SUCH_ALT = DynamicCommandExceptionType { name: Any ->
            Api.TEXT.i18nMessage("argument.alt.error", name)
        }

        operator fun get(context: CommandContext<*>): AltAccount {
            return context.getArgument("alt", AltAccount::class.java)
        }
    }

    override fun parse(reader: StringReader): AltAccount {
        val argument = reader.readString()

        return container.getAltByNameOrNull(argument)
            ?: throw NO_SUCH_ALT.create(argument)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(container.alts.map { it.username }, builder)
    }
}