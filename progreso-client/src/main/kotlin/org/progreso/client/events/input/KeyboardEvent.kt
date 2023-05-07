package org.progreso.client.events.input

import org.progreso.api.event.Event

data class KeyboardEvent(
    val state: Boolean,
    val key: Int,
    val char: Char
) : Event()