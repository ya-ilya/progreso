package org.progreso.irc.packet.packets

import org.progreso.irc.packet.IRCPacket

data class IRCClosePacket(val reason: String) : IRCPacket