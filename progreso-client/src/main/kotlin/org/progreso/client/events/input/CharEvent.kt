package org.progreso.client.events.input

import org.progreso.api.event.Event

data class CharEvent(val codePoint: Int) : Event()