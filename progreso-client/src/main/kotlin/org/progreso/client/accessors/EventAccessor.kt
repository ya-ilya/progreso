package org.progreso.client.accessors

import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.event.EventHandler
import org.progreso.api.event.events.PluginEvent
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.client.Client
import org.progreso.client.events.input.KeyboardEvent
import org.progreso.client.gui.ClickGUI
import org.progreso.client.gui.component.components.CategoryComponent
import org.progreso.client.gui.component.components.ModuleComponent

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

    @EventHandler
    fun onPlugin(event: PluginEvent) {
        if (event.loaded) {
            for (module in event.plugin.modules) {
                CategoryComponent.CATEGORY_COMPONENTS[module.category]?.apply {
                    if (opened) components.removeIf { listComponents.contains(it) }
                    listComponents.add(ModuleComponent(module, ClickGUI.COMPONENT_HEIGHT, this))
                    if (opened) components.addAll(listComponents)
                }
            }
        } else {
            for (module in event.plugin.modules) {
                CategoryComponent.CATEGORY_COMPONENTS[module.category]?.apply {
                    if (opened) components.removeIf { listComponents.contains(it) }
                    listComponents.remove(ModuleComponent.MODULE_COMPONENTS.remove(module) ?: return)
                    if (opened) components.addAll(listComponents)
                }
            }
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        if (CommandManager.onChat(event.message)) {
            event.isCanceled = true
        }
    }
}