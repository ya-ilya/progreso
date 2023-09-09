package org.progreso.irc.event.s2c

import org.progreso.irc.event.IRCEvent

data class CloseS2CEvent(val reason: String) : IRCEvent.S2C