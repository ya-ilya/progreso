package org.progreso.client.command

import net.minecraft.client.Minecraft
import org.progreso.api.command.AbstractCommand

abstract class Command(
    name: String,
    description: String = ""
) : AbstractCommand(name, description) {
    protected companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
    }
}