package org.progreso.api.event

import java.lang.reflect.Method

fun interface EventListener<T : Event> {
    companion object {
        val INVOKE_METHOD: Method = EventListener::class.java
            .getMethod("invoke", Event::class.java)
    }

    fun invoke(event: T)
}