package org.progreso.client.commands.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.client.multiplayer.PlayerInfo
import org.progreso.api.Api
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor
import java.util.concurrent.CompletableFuture

class PlayerArgumentType : ArgumentType<PlayerInfo> {
    companion object {
        private val NO_SUCH_PLAYER = DynamicCommandExceptionType { name: Any ->
            TextAccessor.i18nMessage("argument.player.error", name)
        }

        private val EXAMPLES: Collection<String> = listOf("ya-ilya", "progreso")

        operator fun get(context: CommandContext<*>): PlayerInfo {
            return context.getArgument("player", PlayerInfo::class.java)
        }
    }

    override fun parse(reader: StringReader): PlayerInfo {
        val argument = reader.readString()
        var playerListEntry: PlayerInfo? = null

        for (player in mc.connection!!.onlinePlayers) {
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
        return Api.COMMAND.suggest(mc.connection!!.onlinePlayers.map { it.profile.name }, builder)
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES
    }
}