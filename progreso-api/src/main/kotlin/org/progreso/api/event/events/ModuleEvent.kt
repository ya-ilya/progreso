package org.progreso.api.event.events

import org.progreso.api.event.Event
import org.progreso.api.module.AbstractModule

sealed class ModuleEvent(val module: AbstractModule) : Event() {
    class Toggled(module: AbstractModule) : ModuleEvent(module)
}