package org.progreso.client.accessors

import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.event.EventHandler
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.client.Client
import org.progreso.client.events.input.KeyboardEvent

object EventAccessor : EventAccessor {
    init {
        register(this)
    }

    override fun register(instance: Any) {
        Client.EVENT_BUS.register(instance)
        MinecraftForge.EVENT_BUS.register(instance)
    }

    override fun unregister(instance: Any) {
        Client.EVENT_BUS.unregister(instance)
        MinecraftForge.EVENT_BUS.unregister(instance)
    }

    @EventHandler
    fun onKey(event: KeyboardEvent) {
        if (event.state) {
            ModuleManager.onKey(event.key)
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        if (CommandManager.onChat(event.message)) {
            event.isCanceled = true
        }
    }
}