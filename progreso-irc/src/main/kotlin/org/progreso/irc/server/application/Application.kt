package org.progreso.irc.server.application

import org.java_websocket.WebSocket
import org.progreso.irc.packet.IRCPacket
import org.progreso.irc.packet.packets.IRCAuthPacket
import org.progreso.irc.packet.packets.IRCMessagePacket
import org.progreso.irc.server.IRCServer
import java.net.InetSocketAddress

fun main(args: Array<String>) {
    val server = object : IRCServer(InetSocketAddress(args[0], args[1].toInt())) {
        private val authorized = mutableMapOf<WebSocket, String>()

        override fun onStart() {
            println("Server started")
        }

        override fun onPacket(conn: WebSocket, packet: IRCPacket) {
            when (packet) {
                is IRCAuthPacket -> {
                    if (packet.username.length < 3) {
                        return conn.close("Username length must be >= 3")
                    }

                    if (authorized.containsValue(packet.username)) {
                        return conn.close("User with same username already connected to this IRC server")
                    }

                    authorized[conn] = packet.username

                    println("Authorization: ${packet.username}")
                }

                is IRCMessagePacket -> {
                    val username = authorized[conn] ?: return

                    println("Message from $username: ${packet.message}")
                    send(IRCMessagePacket(username, packet.message))
                }
            }
        }

        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
            authorized.remove(conn)
        }
    }

    val serverThread = Thread(server).also { it.start() }

    while (serverThread.isAlive && !serverThread.isInterrupted) {
        // Server thread
    }
}