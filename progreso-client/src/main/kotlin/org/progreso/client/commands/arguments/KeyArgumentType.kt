package org.progreso.client.commands.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.client.accessors.TextAccessor
import org.progreso.client.util.client.KeyboardUtil
import java.util.concurrent.CompletableFuture

class KeyArgumentType : ArgumentType<Int> {
    companion object {
        private val NO_SUCH_KEY = DynamicCommandExceptionType { name: Any ->
            TextAccessor.i18nMessage("argument.key.error", name)
        }

        operator fun get(context: CommandContext<*>): Int {
            return IntegerArgumentType.getInteger(context, "key")
        }
    }

    override fun parse(reader: StringReader): Int {
        val argument: String = reader.readString()

        return if (KeyboardUtil.keyMap.containsValue(argument)) KeyboardUtil.getKeyCode(argument)
        else throw NO_SUCH_KEY.create(argument)
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(KeyboardUtil.keyMap.values, builder)
    }
}