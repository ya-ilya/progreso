package org.progreso.client.command

import net.minecraft.client.MinecraftClient
import org.progreso.api.command.AbstractCommand
import org.progreso.client.Client

abstract class Command(
    name: String,
    description: String = ""
) : AbstractCommand(name, description) {
    protected companion object {
        val mc: MinecraftClient by lazy { Client.mc }
    }
}