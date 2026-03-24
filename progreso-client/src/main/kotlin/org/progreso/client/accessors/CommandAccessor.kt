package org.progreso.client.accessors

import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.client.multiplayer.ClientSuggestionProvider
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.server.permissions.PermissionSet
import org.progreso.api.accessor.CommandAccessor
import org.progreso.client.Client.Companion.mc
import java.util.concurrent.CompletableFuture

object CommandAccessor : CommandAccessor {
    override fun createSuggestionProvider(): Any {
        return ClientSuggestionProvider(mc.connection!!, mc.client, PermissionSet.ALL_PERMISSIONS)
    }

    override fun suggest(
        candidates: Iterable<String>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return SharedSuggestionProvider.suggest(candidates, builder)
    }
}