package org.progreso.client.manager

import net.minecraft.client.Minecraft
import org.progreso.api.Api

abstract class Manager(events: Boolean = false) {
    protected val mc: Minecraft = Minecraft.getMinecraft()

    init {
        if (events) {
            Api.EVENT.register(this)
        }
    }
}