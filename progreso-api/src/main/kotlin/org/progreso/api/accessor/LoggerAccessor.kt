package org.progreso.api.accessor

/**
 * Provides access to the logger system
 * ```java
 * import org.progreso.api.accessor.LoggerAccessor;
 *
 * public class LoggerAccessorImpl implements LoggerAccessor {
 *     @Override
 *     public void debug(String message) {
 *         LOGGER.debug(message);
 *     }
 *
 *     @Override
 *     public void info(String message) {
 *         LOGGER.info(message);
 *     }
 *
 *     @Override
 *     public void warn(String message, Throwable throwable) {
 *         if (throwable != null) {
 *             LOGGER.warn(message, throwable);
 *         } else {
 *             LOGGER.warn(message);
 *         }
 *     }
 *
 *     @Override
 *     public void error(String message, Throwable throwable) {
 *         if (throwable != null) {
 *             LOGGER.error(message, throwable);
 *         } else {
 *             LOGGER.error(message);
 *         }
 *     }
 * }
 * ```
 */
interface LoggerAccessor {
    open class Default : LoggerAccessor {
        override fun debug(message: String) {}
        override fun info(message: String) {}
        override fun warn(message: String, throwable: Throwable?) {}
        override fun error(message: String, throwable: Throwable?) {}
    }

    /**
     * Log message with DEBUG log-level
     *
     * @param message Debug message
     */
    fun debug(message: String)

    /**
     * Log message with INFO log-level
     *
     * @param message Info message
     */
    fun info(message: String)

    /**
     * Log message with WARN log-level
     *
     * @param message Warn message
     * @param throwable Error
     */
    fun warn(message: String, throwable: Throwable? = null)

    /**
     * Log message with ERROR log-level
     *
     * @param message Error message
     * @param throwable Error
     */
    fun error(message: String, throwable: Throwable? = null)
}