package org.progreso.api.accessor

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
     * Sends info message to the chat
     */
    fun info(message: Any)

    /**
     * Sends warn message to the chat
     */
    fun warn(message: Any)

    /**
     * Sends error message to the chat
     */
    fun error(message: Any)

    /**
     * Add message to sent messages
     */
    fun addToSentMessages(message: Any)
}