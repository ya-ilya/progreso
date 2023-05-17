package org.progreso.irc.server

import org.java_websocket.WebSocket
import org.progreso.api.irc.IRCServer
import org.progreso.api.irc.packet.IRCPacket
import org.progreso.api.irc.packet.packets.IRCAuthFailedPacket
import org.progreso.api.irc.packet.packets.IRCAuthPacket
import org.progreso.api.irc.packet.packets.IRCMessagePacket
import java.net.InetSocketAddress

fun main(args: Array<String>) {
    val server = object : IRCServer(InetSocketAddress(args[0], args[1].toInt())) {
        private val authorized = mutableMapOf<WebSocket, String>()

        override fun onStart() {
            println("Server started")
        }

        override fun onPacket(socket: WebSocket, packet: IRCPacket) {
            when (packet) {
                is IRCAuthPacket -> {
                    if (packet.username.length < 3) {
                        socket.send(IRCAuthFailedPacket("Username length must be >= 3"))
                        socket.close()
                        return
                    }

                    if (authorized.containsValue(packet.username)) {
                        socket.send(IRCAuthFailedPacket("User with same username already connected to this IRC server"))
                        socket.close()
                        return
                    }

                    authorized[socket] = packet.username

                    println("Authorization: ${packet.username}")
                }

                is IRCMessagePacket -> {
                    val username = authorized[socket] ?: return

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