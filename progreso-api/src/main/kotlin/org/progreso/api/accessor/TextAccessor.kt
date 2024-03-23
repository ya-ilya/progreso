package org.progreso.api.accessor

import com.mojang.brigadier.Message

/**
 * Provides access to the internationalization system
 * ```java
 * import org.progreso.api.accessor.TextAccessor;
 *
 * public class TextAccessorImpl implements TextAccessor {
 *     @Override
 *     public String i18n(String key, Object... args) {
 *         return Text.translatable(key, args).getString();
 *     }
 * }
 * ```
 */
interface TextAccessor {
    open class Default : TextAccessor {
        override fun i18n(key: String, vararg args: Any) = key
        override fun i18nMessage(key: String, vararg args: Any) = Message { i18n(key, args) }
    }

    /**
     * Get internationalization by [key]
     *
     * @param key Key
     * @param args Arguments
     * @see [String.format]
     */
    fun i18n(key: String, vararg args: Any): String

    /**
     * Get internationalization by [key]
     *
     * @param key Key
     * @param args Arguments
     * @see [String.format]
     */
    fun i18nMessage(key: String, vararg args: Any): Message
}