package org.progreso.irc.application

import org.java_websocket.WebSocket
import org.progreso.irc.IRCServer
import org.progreso.irc.event.IRCEvent
import org.progreso.irc.event.c2s.MessageC2SEvent
import org.progreso.irc.event.c2s.RegisterC2SEvent
import org.progreso.irc.event.s2c.MessageS2CEvent
import java.net.InetSocketAddress
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    val server = object : IRCServer(InetSocketAddress(args[0], args[1].toInt())) {
        private val authorized = mutableMapOf<WebSocket, String>()

        override fun onStart() {
            println("Server started")
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

                    println("${event.username} authorized")
                }

                is MessageC2SEvent -> {
                    val username = authorized[connection] ?: return

                    println("Message from $username: ${event.message}")
                    send(MessageS2CEvent(username, event.message))
                }
            }
        }

        override fun onClose(connection: WebSocket) {
            if (authorized.containsKey(connection)) {
                println("${authorized[connection]} disconnected")
            }
        }
    }

    val serverThread = thread { server.start() }

    while (serverThread.isAlive && !serverThread.isInterrupted) {
        // Server thread
    }
}