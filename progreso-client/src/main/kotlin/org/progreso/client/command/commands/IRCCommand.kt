package org.progreso.client.command.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.java_websocket.handshake.ServerHandshake
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

    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(literal("connect").then(
            argument("address", StringArgumentType.greedyString()).executesSuccess { context ->
                val address = StringArgumentType.getString(context, "address")

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
        ))

        builder.then(literal("disconnect").executesSuccess {
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@executesSuccess sendIRCInfo("Client isn't connected to the server")
            }

            client?.close()
            client = null
        })

        builder.then(literal("send").then(
            argument("message", StringArgumentType.greedyString()).executesSuccess { context ->
                if (client == null || client?.isClosed == true || client?.isOpen == false) {
                    return@executesSuccess sendIRCInfo("Client isn't connected to the server")
                }

                client?.send(IRCMessagePacket(mc.player.name, StringArgumentType.getString(context, "message")))
            }
        ))
    }
}