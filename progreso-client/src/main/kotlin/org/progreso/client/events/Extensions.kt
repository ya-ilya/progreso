package org.progreso.client.events

import org.progreso.api.event.Event
import org.progreso.api.event.EventListener
import org.progreso.api.event.EventPriority
import org.progreso.client.Client
import org.progreso.client.Client.Companion.mc

@Suppress("RedundantSamConstructor")
inline fun <reified T : Event> Any.eventListener(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline block: (T) -> Unit
) {
    Client.EVENT_BUS.registerListener(
        instanceClass = javaClass,
        eventClass = T::class.java,
        priority = priority,
        listener = EventListener<T> {
            block(it)
        }
    )
}

inline fun <reified T : Event> Any.safeEventListener(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline block: (T) -> Unit
) {
    eventListener<T>(priority) {
        if (!mc.isNotSafe()) {
            block(it)
        }
    }
}