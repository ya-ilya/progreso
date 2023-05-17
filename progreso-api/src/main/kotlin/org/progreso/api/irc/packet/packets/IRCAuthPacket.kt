package org.progreso.api.irc.packet.packets

import org.progreso.api.irc.packet.IRCPacket

data class IRCAuthPacket(val username: String) : IRCPacket