package org.progreso.client.command.commands

import org.java_websocket.handshake.ServerHandshake
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.api.command.argument.ArgumentBuilder
import org.progreso.api.irc.IRCClient
import org.progreso.api.irc.packet.IRCPacket
import org.progreso.api.irc.packet.packets.IRCAuthFailedPacket
import org.progreso.api.irc.packet.packets.IRCAuthPacket
import org.progreso.api.irc.packet.packets.IRCMessagePacket
import org.progreso.client.command.Command
import java.net.URI

class IRCCommand : Command("irc") {
    private companion object {
        var client: IRCClient? = null
    }

    private fun sendIRCInfo(message: String) {
        send("[IRC] $message")
    }

    override fun build(builder: ArgumentBuilder) {
        builder.literal("connect") {
            argument("address", string()).executes { context ->
                val address = context.get<String>("address")

                if (client?.isOpen == true && client?.isClosed == false) client?.close()
                client = object : IRCClient(URI.create(address)) {
                    init {
                        try {
                            connect()
                        } catch (ex: Exception) {
                            sendIRCInfo("Failed to connect to the server")
                        }
                    }

                    override fun onPacket(packet: IRCPacket) {
                        when (packet) {
                            is IRCAuthFailedPacket -> {
                                sendIRCInfo("Authentication failed. Reason: ${packet.reason}")
                                close()
                            }

                            is IRCMessagePacket -> {
                                sendIRCInfo("${packet.author}: ${packet.message}")
                            }
                        }
                    }

                    override fun onOpen(handshakedata: ServerHandshake) {
                        send(IRCAuthPacket(mc.player.name))
                        sendIRCInfo("Connected to $address")
                    }

                    override fun onClose(code: Int, reason: String, remote: Boolean) {
                        sendIRCInfo("Disconnected")
                    }
                }
            }
        }

        builder.literal("disconnect").executes {
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@executes sendIRCInfo("Client isn't connected to the server")
            }

            client?.close()
            client = null
        }

        builder.literal("send") {
            argument("message", string()).executes { context ->
                if (client == null || client?.isClosed == true || client?.isOpen == false) {
                    return@executes sendIRCInfo("Client isn't connected to the server")
                }

                client?.send(IRCMessagePacket(mc.player.name, context.get<String>("message")))
            }
        }
    }
}