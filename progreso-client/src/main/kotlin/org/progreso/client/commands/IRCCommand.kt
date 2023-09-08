package org.progreso.client.commands

import org.java_websocket.handshake.ServerHandshake
import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.client.Client.Companion.mc
import org.progreso.irc.client.IRCClient
import org.progreso.irc.packet.IRCPacket
import org.progreso.irc.packet.packets.IRCAuthFailedPacket
import org.progreso.irc.packet.packets.IRCAuthPacket
import org.progreso.irc.packet.packets.IRCMessagePacket
import java.net.URI

@AbstractCommand.Register("irc")
object IRCCommand : AbstractCommand() {
    private var client: IRCClient? = null

    init {
        literal("connect").argument("address", string()).executes { context ->
            val address: String by context

            if (client?.isOpen == true && client?.isClosed == false) client?.close()
            client = object : IRCClient(URI.create(address)) {
                init {
                    try {
                        connect()
                    } catch (ex: Exception) {
                        errorLocalized("command.irc.connect_error")
                    }
                }

                override fun onPacket(packet: IRCPacket) {
                    when (packet) {
                        is IRCAuthFailedPacket -> {
                            errorLocalized(
                                "command.irc.auth_error",
                                packet.reason
                            )
                            close()
                        }

                        is IRCMessagePacket -> {
                            infoLocalized(
                                "command.irc.message",
                                packet.author,
                                packet.message
                            )
                        }
                    }
                }

                override fun onOpen(handshakedata: ServerHandshake) {
                    send(IRCAuthPacket(mc.player.name.string))
                    infoLocalized(
                        "command.irc.connect",
                        address
                    )
                }

                override fun onClose(code: Int, reason: String, remote: Boolean) {
                    infoLocalized("command.irc.disconnect")
                }
            }
        }

        literal("disconnect").executes { _ ->
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@executes errorLocalized("command.irc.disconnect_error")
            }

            client?.close()
            client = null
        }

        literal("send").argument("message", string(true)).executes { context ->
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@executes errorLocalized("command.irc.disconnect_error")
            }

            client?.send(IRCMessagePacket(mc.player.name.string, context.get("message")))
        }
    }
}