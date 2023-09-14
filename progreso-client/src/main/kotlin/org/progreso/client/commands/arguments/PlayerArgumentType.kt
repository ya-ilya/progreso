package org.progreso.client.commands.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.client.network.PlayerListEntry
import org.progreso.api.Api
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor
import java.util.concurrent.CompletableFuture

class PlayerArgumentType : ArgumentType<PlayerListEntry> {
    companion object {
        private val NO_SUCH_PLAYER = DynamicCommandExceptionType { name: Any ->
            TextAccessor.i18nMessage("argument.player.error", name)
        }

        private val EXAMPLES: Collection<String> = listOf("ya-ilya", "progreso")

        operator fun get(context: CommandContext<*>): PlayerListEntry {
            return context.getArgument("player", PlayerListEntry::class.java)
        }
    }

    override fun parse(reader: StringReader): PlayerListEntry {
        val argument: String = reader.readString()
        var playerListEntry: PlayerListEntry? = null

        for (player in mc.networkHandler.playerList) {
            if (player.profile.name.equals(argument, ignoreCase = true)) {
                playerListEntry = player
                break
            }
        }

        if (playerListEntry == null) throw NO_SUCH_PLAYER.create(argument)

        return playerListEntry
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return Api.COMMAND.suggestMatching(mc.networkHandler.playerList.map { it.profile.name }, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}