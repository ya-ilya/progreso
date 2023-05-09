package org.progreso.client.events

import net.minecraft.client.Minecraft
import org.progreso.api.event.Event
import org.progreso.api.event.EventListener
import org.progreso.api.event.EventPriority
import org.progreso.client.Client

val mc: Minecraft = Minecraft.getMinecraft()

@Suppress("RedundantSamConstructor")
inline fun <reified T : Event> Any.eventListener(crossinline block: (T) -> Unit) {
    Client.EVENT_BUS.registerListener(
        instanceClass = javaClass,
        eventClass = T::class.java,
        priority = EventPriority.NORMAL,
        listener = EventListener<T> {
            block(it)
        }
    )
}

inline fun <reified T : Event> Any.safeEventListener(crossinline block: (T) -> Unit) {
    eventListener<T> {
        if (mc.player != null && mc.world != null) {
            block(it)
        }
    }
}