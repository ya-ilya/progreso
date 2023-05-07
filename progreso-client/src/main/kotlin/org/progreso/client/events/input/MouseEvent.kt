package org.progreso.client.events.input

import org.progreso.api.event.Event

data class MouseEvent(
    val state: Boolean,
    val button: Int
) : Event()