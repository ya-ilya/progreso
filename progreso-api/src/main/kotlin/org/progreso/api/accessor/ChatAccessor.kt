package org.progreso.api.accessor

import org.progreso.api.Api

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
    open class Default : ChatAccessor {
        override fun send(message: Any) {}
        override fun info(message: Any) {}
        override fun warn(message: Any) {}
        override fun error(message: Any) {}
        override fun addToSentMessages(message: Any) {}
    }

    /**
     * Send message to the chat
     */
    fun send(message: Any)

    /**
     * Send localized message to the chat
     */
    fun sendLocalized(key: String, vararg args: Any) {
        send(Api.TEXT.i18n(key, *args))
    }

    /**
     * Sends info message to the chat
     */
    fun info(message: Any)

    /**
     * Send localized info message to the chat
     */
    fun infoLocalized(key: String, vararg args: Any) {
        info(Api.TEXT.i18n(key, *args))
    }

    /**
     * Sends warn message to the chat
     */
    fun warn(message: Any)

    /**
     * Send localized warn message to the chat
     */
    fun warnLocalized(key: String, vararg args: Any) {
        warn(Api.TEXT.i18n(key, *args))
    }

    /**
     * Sends error message to the chat
     */
    fun error(message: Any)

    /**
     * Send localized error message to the chat
     */
    fun errorLocalized(key: String, vararg args: Any) {
        error(Api.TEXT.i18n(key, *args))
    }

    /**
     * Add message to sent messages
     */
    fun addToSentMessages(message: Any)
}