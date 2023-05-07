package org.progreso.api.event

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class EventHandler(val priority: EventPriority = EventPriority.NORMAL)
