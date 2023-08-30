package org.progreso.client.events.player

import org.progreso.api.event.Event

data class ClipAtLedgeEvent(var clip: Boolean? = null) : Event()