package org.progreso.client.events.client

import org.progreso.api.event.Event
import org.progreso.api.module.AbstractModule

data class ModuleEvent(val module: AbstractModule) : Event()