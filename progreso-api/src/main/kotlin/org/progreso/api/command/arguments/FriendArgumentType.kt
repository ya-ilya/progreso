package org.progreso.api.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import org.progreso.api.Api
import org.progreso.api.managers.FriendManager
import java.util.concurrent.CompletableFuture

class FriendArgumentType : ArgumentType<FriendManager.Friend> {
    companion object {
        private val NO_SUCH_FRIEND = DynamicCommandExceptionType { name: Any ->
            Api.TEXT.i18nMessage("argument.friend.error", name)
        }

        private val EXAMPLES = listOf("ya-ilya", "progreso")

        operator fun get(context: CommandContext<*>): FriendManager.Friend {
            return context.getArgument("friend", FriendManager.Friend::class.java)
        }
    }

    override fun parse(reader: StringReader): FriendManager.Friend {
        val friendName = reader.readString()
        return FriendManager.getFriendByNameOrNull(friendName) ?: throw NO_SUCH_FRIEND.create(friendName)
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(FriendManager.friends.map { it.name }, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}