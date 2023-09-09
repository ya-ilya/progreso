package org.progreso.irc.event.c2s

import org.progreso.irc.event.IRCEvent

data class MessageC2SEvent(val message: String) : IRCEvent.C2S