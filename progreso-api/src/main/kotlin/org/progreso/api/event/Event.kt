package org.progreso.api.event

abstract class Event {
    var isCancelled: Boolean = false

    fun cancel() {
        isCancelled = true
    }
}