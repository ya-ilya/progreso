package org.progreso.api.accessor

/**
 * Provides access to the minecraft text class
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
    /**
     * Get internationalization by [key]
     *
     * @param args Arguments
     * @see [String.format]
     */
    fun i18n(key: String, vararg args: Any): String
}