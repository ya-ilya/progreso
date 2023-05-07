package org.progreso.client.accessors

import org.progreso.api.accessor.LoggerAccessor
import org.progreso.client.Client

object LoggerAccessor : LoggerAccessor {
    override fun debug(message: String) {
        Client.LOGGER.debug(message)
    }

    override fun info(message: String) {
        Client.LOGGER.info(message)
    }

    override fun warn(message: String, throwable: Throwable?) {
        if (throwable != null) Client.LOGGER.warn(message, throwable)
        else Client.LOGGER.warn(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        if (throwable != null) Client.LOGGER.error(message, throwable)
        else Client.LOGGER.error(message)
    }
}