package org.progreso.irc.packet.packets

import org.progreso.irc.packet.IRCPacket

data class IRCAuthFailedPacket(val reason: String) : IRCPacket