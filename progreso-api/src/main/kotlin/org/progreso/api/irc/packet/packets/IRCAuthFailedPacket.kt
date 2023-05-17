package org.progreso.api.irc.packet.packets

import org.progreso.api.irc.packet.IRCPacket

data class IRCAuthFailedPacket(val reason: String) : IRCPacket