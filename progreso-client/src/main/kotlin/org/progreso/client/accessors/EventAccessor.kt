package org.progreso.client.accessors

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.server.SPacketEntityStatus
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.api.accessor.EventAccessor
import org.progreso.api.event.events.PluginEvent
import org.progreso.api.managers.CommandManager
import org.progreso.api.managers.ModuleManager
import org.progreso.client.Client
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.eventListener
import org.progreso.client.events.input.KeyboardEvent
import org.progreso.client.events.network.PacketEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.component.components.CategoryComponent
import org.progreso.client.gui.clickgui.component.components.ModuleComponent
import org.progreso.client.gui.mc.ProgresoGuiChat

object EventAccessor : EventAccessor {
    private val mc: Minecraft = Minecraft.getMinecraft()

    override fun register(instance: Any) {
        Client.EVENT_BUS.register(instance)
        MinecraftForge.EVENT_BUS.register(instance)
    }

    override fun unregister(instance: Any) {
        Client.EVENT_BUS.unregister(instance)
        MinecraftForge.EVENT_BUS.unregister(instance)
    }

    init {
        register(this)

        eventListener<KeyboardEvent> { event ->
            if (!event.state) return@eventListener
            ModuleManager.onKey(event.key)

            if (!mc.gameSettings.keyBindSneak.isKeyDown) {
                if (event.char.equals(CommandManager.PREFIX, true)) {
                    mc.displayGuiScreen(ProgresoGuiChat(event.char.toString()))
                }
            }
        }

        eventListener<PluginEvent> { event ->
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
                        listComponents.remove(ModuleComponent.MODULE_COMPONENTS.remove(module) ?: return@eventListener)
                        if (opened) components.addAll(listComponents)
                    }
                }
            }
        }

        eventListener<PacketEvent.Receive<*>> { event ->
            if (event.packet is SPacketEntityStatus && event.packet.opCode.toInt() == 35) {
                val entity = event.packet.getEntity(mc.world)

                if (entity is EntityPlayer) {
                    Client.EVENT_BUS.post(TotemPopEvent(entity))
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

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        event.isCanceled = Client.EVENT_BUS.post(EntityDeathEvent(event.entity))
    }
}