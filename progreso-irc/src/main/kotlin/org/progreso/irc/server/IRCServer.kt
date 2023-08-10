package org.progreso.irc.server

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.progreso.irc.packet.IRCPacket
import java.net.InetSocketAddress

open class IRCServer(address: InetSocketAddress) : WebSocketServer(address) {
    open fun onPacket(socket: WebSocket, packet: IRCPacket) {}

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {}
    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {}
    override fun onError(conn: WebSocket?, ex: Exception) {}
    override fun onStart() {}

    override fun onMessage(conn: WebSocket, message: String) {
        onPacket(conn, IRCPacket.GSON.fromJson(message, IRCPacket::class.java))
    }

    fun send(packet: IRCPacket) {
        connections.forEach { it.send(packet) }
    }

    fun WebSocket.send(packet: IRCPacket) {
        send(IRCPacket.GSON.toJson(packet, IRCPacket::class.java))
    }
}