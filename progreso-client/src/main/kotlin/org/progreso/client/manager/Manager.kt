package org.progreso.client.manager

import org.progreso.api.Api
import org.progreso.client.Client

abstract class Manager(events: Boolean = false) {
    protected companion object {
        val mc by lazy { Client.mc }
    }

    init {
        if (events) {
            Api.EVENT.register(this)
        }
    }
}