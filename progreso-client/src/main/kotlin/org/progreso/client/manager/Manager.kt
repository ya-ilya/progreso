package org.progreso.client.manager

import net.minecraft.client.MinecraftClient
import org.progreso.api.Api
import org.progreso.client.Client

abstract class Manager(events: Boolean = false) {
    protected companion object {
        val mc: MinecraftClient by lazy { Client.mc }
    }

    init {
        if (events) {
            Api.EVENT.register(this)
        }
    }
}