package org.progreso.irc.packet.packets

import org.progreso.irc.packet.IRCPacket

data class IRCMessagePacket(val author: String, val message: String) : IRCPacket