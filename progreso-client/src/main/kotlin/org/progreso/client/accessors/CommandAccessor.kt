package org.progreso.client.accessors

import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.client.network.ClientCommandSource
import net.minecraft.command.CommandSource
import org.progreso.api.accessor.CommandAccessor
import org.progreso.client.Client.Companion.mc
import java.util.concurrent.CompletableFuture

object CommandAccessor : CommandAccessor {
    override fun createCommandSource(): Any {
        return ClientCommandSource(null, mc.client)
    }

    override fun suggestMatching(
        candidates: Iterable<String>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return CommandSource.suggestMatching(candidates, builder)
    }
}