package org.progreso.client.command

import org.progreso.api.command.AbstractCommand
import org.progreso.client.Client

abstract class Command(
    name: String,
    description: String = ""
) : AbstractCommand(name, description) {
    protected companion object {
        val mc by lazy { Client.mc }
    }
}