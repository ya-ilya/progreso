package org.progreso.api.accessor

import org.progreso.api.i18n.I18n.i18n

/**
 * Provides access to the minecraft client chat
 * ```java
 * import org.progreso.api.accessor.ChatAccessor;
 *
 * public class ChatAccessorImpl implements ChatAccessor {
 *     @Override
 *     public void send(Object message) {
 *         mc.player.sendMessage(TextComponentString(message.toString()));
 *     }
 *
 *     @Override
 *     public void info(Object message) {
 *         send(ChatFormatting.RED + message.toString());
 *     }
 *
 *     @Override
 *     public void warn(Object message) {
 *         send(ChatFormatting.YELLOW + message.toString());
 *     }
 *
 *     @Override
 *     public void error(Object message) {
 *         send(ChatFormatting.RED + message.toString());
 *     }
 *
 *     @Override
 *     public void addToSentMessages(Object message) {
 *         mc.ingameGUI.chatGUI.addToSentMessages(message.toString());
 *     }
 * }
 * ```
 */
interface ChatAccessor {
    /**
     * Send message to the chat
     */
    fun send(message: Any)

    /**
     * Send localized message to the chat
     */
    fun sendLocalized(key: String, vararg replacements: Pair<String, Any>) {
        send(i18n(key, *replacements))
    }

    /**
     * Sends info message to the chat
     */
    fun info(message: Any)

    /**
     * Send localized info message to the chat
     */
    fun infoLocalized(key: String, vararg replacements: Pair<String, Any>) {
        info(i18n(key, *replacements))
    }

    /**
     * Sends warn message to the chat
     */
    fun warn(message: Any)

    /**
     * Send localized warn message to the chat
     */
    fun warnLocalized(key: String, vararg replacements: Pair<String, Any>) {
        warn(i18n(key, *replacements))
    }

    /**
     * Sends error message to the chat
     */
    fun error(message: Any)

    /**
     * Send localized error message to the chat
     */
    fun errorLocalized(key: String, vararg replacements: Pair<String, Any>) {
        error(i18n(key, *replacements))
    }

    /**
     * Add message to sent messages
     */
    fun addToSentMessages(message: Any)
}