package org.progreso.irc

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.progreso.irc.event.IRCEvent
import java.net.URI

open class IRCClient(address: String) : WebSocketClient(URI.create(address)) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        onOpen()
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        onClose()
    }

    override fun onError(ex: Exception?) {}

    override fun onMessage(message: String?) {
        onEvent(IRCEvent.S2C.fromJson(message ?: return))
    }

    open fun onOpen() {}
    open fun onClose() {}
    open fun onEvent(event: IRCEvent.S2C) {}

    fun send(event: IRCEvent.C2S) {
        send(IRCEvent.C2S.toJson(event))
    }
}