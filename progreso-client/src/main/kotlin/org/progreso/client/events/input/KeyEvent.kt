package org.progreso.client.events.input

import org.progreso.api.event.Event

data class KeyEvent(
    val key: Int,
    val scanCode: Int,
    val action: Int
) : Event()