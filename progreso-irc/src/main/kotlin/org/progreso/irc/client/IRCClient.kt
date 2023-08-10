package org.progreso.irc.client

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.progreso.irc.packet.IRCPacket
import java.net.URI

open class IRCClient(serverUri: URI) : WebSocketClient(serverUri) {
    open fun onPacket(packet: IRCPacket) {}

    override fun onOpen(handshakedata: ServerHandshake) {}
    override fun onClose(code: Int, reason: String, remote: Boolean) {}
    override fun onError(ex: Exception) {}

    override fun onMessage(message: String) {
        onPacket(IRCPacket.GSON.fromJson(message, IRCPacket::class.java))
    }

    fun send(packet: IRCPacket) {
        send(IRCPacket.GSON.toJson(packet, IRCPacket::class.java))
    }
}