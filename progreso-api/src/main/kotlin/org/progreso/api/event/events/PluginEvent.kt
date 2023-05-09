package org.progreso.api.event.events

import org.progreso.api.event.Event
import org.progreso.api.plugin.AbstractPlugin

data class PluginEvent(val plugin: AbstractPlugin, val loaded: Boolean) : Event()