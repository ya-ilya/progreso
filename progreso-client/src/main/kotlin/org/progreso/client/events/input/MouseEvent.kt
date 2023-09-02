package org.progreso.client.events.input

import org.progreso.api.event.Event

data class MouseEvent(
    val button: Int,
    val action: Int
) : Event()