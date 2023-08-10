package org.progreso.irc.packet.packets

import org.progreso.irc.packet.IRCPacket

data class IRCAuthPacket(val username: String) : IRCPacket