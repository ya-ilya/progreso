package org.progreso.api.irc.packet.packets

import org.progreso.api.irc.packet.IRCPacket

data class IRCMessagePacket(val author: String, val message: String) : IRCPacket