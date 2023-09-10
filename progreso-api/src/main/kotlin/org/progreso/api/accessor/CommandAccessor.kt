package org.progreso.api.accessor

import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

/**
 * Provides access to the minecraft command system
 * ```java
 * import org.progreso.api.accessor.CommandAccessor;
 *
 * public class CommandAccessorImpl implements CommandAccessor {
 *     @Override
 *     public Object createCommandSource() {
 *         return new ClientCommandSource(null, mc)
 *     }
 *
 *     @Override
 *     public <S> CompletableFuture<Suggestions> suggestMatching(Iterable<String> candidates, SuggestionsBuilder builder) {
 *         return CommandSource.suggestMatching(args, builder);
 *     }
 * }
 */
interface CommandAccessor {
    open class Default : CommandAccessor {
        override fun createCommandSource(): Any {
            return Any()
        }

        override fun suggestMatching(
            candidates: Iterable<String>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            return CompletableFuture()
        }
    }

    fun createCommandSource(): Any

    fun suggestMatching(candidates: Iterable<String>, builder: SuggestionsBuilder): CompletableFuture<Suggestions>
}