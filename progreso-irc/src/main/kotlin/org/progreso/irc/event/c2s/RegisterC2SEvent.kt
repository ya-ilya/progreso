package org.progreso.irc.event.c2s

import org.progreso.irc.event.IRCEvent

data class RegisterC2SEvent(val username: String) : IRCEvent.C2S