package org.progreso.irc.event.s2c

import org.progreso.irc.event.IRCEvent

data class MessageS2CEvent(val author: String, val message: String) : IRCEvent.S2C