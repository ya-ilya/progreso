package org.progreso.client.commands

import com.mojang.brigadier.arguments.StringArgumentType
import org.progreso.api.command.AbstractCommand
import org.progreso.client.Client.Companion.mc
import org.progreso.irc.IRCClient
import org.progreso.irc.event.IRCEvent
import org.progreso.irc.event.c2s.MessageC2SEvent
import org.progreso.irc.event.c2s.RegisterC2SEvent
import org.progreso.irc.event.s2c.CloseS2CEvent
import org.progreso.irc.event.s2c.MessageS2CEvent

@AbstractCommand.Register("irc")
object IRCCommand : AbstractCommand() {
    private var client: IRCClient? = null
        set(value) {
            field?.close()
            field = value
        }

    init {
        builder.then(
            literal("connect").then(
                argument("address", StringArgumentType.string()).execute { context ->
                    val address = StringArgumentType.getString(context, "address")

                    client = object : IRCClient(address) {
                        init {
                            try {
                                connect()
                            } catch (ex: Exception) {
                                errorLocalized("command.irc.connect_error")
                            }
                        }

                        override fun onEvent(event: IRCEvent.S2C) {
                            when (event) {
                                is CloseS2CEvent -> {
                                    errorLocalized(
                                        "command.irc.close",
                                        event.reason
                                    )
                                    close()
                                }

                                is MessageS2CEvent -> {
                                    infoLocalized(
                                        "command.irc.message",
                                        event.author,
                                        event.message
                                    )
                                }
                            }
                        }

                        override fun onOpen() {
                            send(RegisterC2SEvent(mc.player.name.string))
                            infoLocalized(
                                "command.irc.connect",
                                address
                            )
                        }

                        override fun onClose() {
                            infoLocalized("command.irc.disconnect", address)
                        }
                    }
                }
            )
        )

        builder.then(literal("disconnect").execute {
            if (client == null || client?.isClosed == true || client?.isOpen == false) {
                return@execute errorLocalized("command.irc.disconnect_error")
            }

            client = null
        })

        builder.then(
            literal("send").then(argument("message", StringArgumentType.greedyString())).execute { context ->
                if (client == null || client?.isClosed == true || client?.isOpen == false) {
                    return@execute errorLocalized("command.irc.disconnect_error")
                }

                client?.send(MessageC2SEvent(StringArgumentType.getString(context, "message")))
            }
        )
    }
}