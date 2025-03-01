package org.progreso.irc.application

import org.java_websocket.WebSocket
import org.progreso.irc.IRCServer
import org.progreso.irc.event.IRCEvent
import org.progreso.irc.event.c2s.MessageC2SEvent
import org.progreso.irc.event.c2s.RegisterC2SEvent
import org.progreso.irc.event.s2c.MessageS2CEvent
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

val SERVER_PORT: Int = System.getenv("SERVER_PORT").toInt()

fun main() {
    val server = object : IRCServer(SERVER_PORT) {
        private val logger = LoggerFactory.getLogger(javaClass)

        private val authorized = mutableMapOf<WebSocket, String>()

        override fun onStart() {
            logger.info("Server started")
        }

        override fun onEvent(connection: WebSocket, event: IRCEvent.C2S) {
            when (event) {
                is RegisterC2SEvent -> {
                    if (event.username.length < 3) {
                        return connection.close("Username length must be >= 3")
                    }

                    if (authorized.containsValue(event.username)) {
                        return connection.close("User with same username already connected to this IRC server")
                    }

                    authorized[connection] = event.username

                    logger.info("${event.username} authorized")
                }

                is MessageC2SEvent -> {
                    val username = authorized[connection] ?: return

                    logger.info("Message from $username: ${event.message}")
                    send(MessageS2CEvent(username, event.message))
                }
            }
        }

        override fun onOpen(connection: WebSocket) {
            logger.info("New connection: ${connection.localSocketAddress}")
        }

        override fun onClose(connection: WebSocket) {
            if (authorized.containsKey(connection)) {
                logger.info("${authorized[connection]} disconnected")
            }
        }
    }

    val serverThread = thread { server.start() }

    while (serverThread.isAlive && !serverThread.isInterrupted) {
        // Server thread
    }
}