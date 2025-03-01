package org.progreso.irc

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import org.progreso.irc.event.IRCEvent
import java.net.InetSocketAddress

open class IRCServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
    override fun onStart() {}

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        onOpen(conn)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        onClose(conn)
    }

    override fun onError(conn: WebSocket, ex: Exception) {}

    override fun onMessage(conn: WebSocket, message: String?) {
        onEvent(conn, IRCEvent.C2S.fromJson(message ?: return))
    }

    open fun onOpen(connection: WebSocket) {}
    open fun onClose(connection: WebSocket) {}
    open fun onEvent(connection: WebSocket, event: IRCEvent.C2S) {}

    fun send(event: IRCEvent.S2C) {
        connections.forEach { it.send(IRCEvent.S2C.toJson(event)) }
    }

    fun WebSocket.close(reason: String) {
        send(reason)
        close()
    }
}