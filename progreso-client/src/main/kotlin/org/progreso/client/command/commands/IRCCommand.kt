package org.progreso.client.command.commands

import org.java_websocket.handshake.ServerHandshake
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.irc.IRCClient
import org.progreso.api.irc.packet.IRCPacket
import org.progreso.api.irc.packet.packets.IRCAuthFailedPacket
import org.progreso.api.irc.packet.packets.IRCAuthPacket
import org.progreso.api.irc.packet.packets.IRCMessagePacket
import org.progreso.client.command.Command
import java.net.URI

object IRCCommand : Command("irc") {
    private var client: IRCClient? = null

    init {
        literal("connect") {
            argument("address", string()).executes { context ->
                val address: String by context

                if (client?.isOpen == true && client?.isClosed == false) client?.close()
                client = object : IRCClient(URI.create(address)) {
                    init {
                        try {
                            connect()
                        } catch (ex: Exception) {
                            error("[IRC] Failed to connect to the server")
                        }
                    }

                    override fun onPacket(packet: IRCPacket) {
                        when (packet) {
                            is IRCAuthFailedPacket -> {
                                error("[IRC] Authentication failed. Reason: ${packet.reason}")
                                close()
                            }

                            is IRCMessagePacket -> {
                                info("[IRC] ${packet.author}: ${packet.message}")
                            }
                        }
                    }

                    override fun onOpen(handshakedata: ServerHandshake) {
                        send(IRCAuthPacket(mc.player!!.name.string))
                        info("[IRC] Connected to $address")
                    }

                    override fun onClose(code: Int, reason: String, remote: Boolean) {
                        info("[IRC] Disconnected")
                    }
                }
            }
        }

        literal("disconnect").executes {
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@executes error("[IRC] Client isn't connected to the server")
            }

            client?.close()
            client = null
        }

        literal("send") {
            argument("message", string()).executes { context ->
                if (client == null || client?.isClosed == true || client?.isOpen == false) {
                    return@executes error("[IRC] Client isn't connected to the server")
                }

                client?.send(IRCMessagePacket(mc.player!!.name.string, context.get("message")))
            }
        }
    }
}