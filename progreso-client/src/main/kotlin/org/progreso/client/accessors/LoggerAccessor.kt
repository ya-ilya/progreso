package org.progreso.client.accessors

import org.progreso.api.accessor.LoggerAccessor
import org.progreso.client.Client.Companion.LOGGER

object LoggerAccessor : LoggerAccessor {
    override fun debug(message: String) {
        LOGGER.debug(message)
    }

    override fun info(message: String) {
        LOGGER.info(message)
    }

    override fun warn(message: String, throwable: Throwable?) {
        if (throwable != null) LOGGER.warn(message, throwable)
        else LOGGER.warn(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        if (throwable != null) LOGGER.error(message, throwable)
        else LOGGER.error(message)
    }
}