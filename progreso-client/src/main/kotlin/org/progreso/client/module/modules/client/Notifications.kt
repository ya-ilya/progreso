package org.progreso.client.module.modules.client

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.entity.player.EntityPlayer
import org.progreso.api.Api
import org.progreso.client.events.client.ModuleEvent
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.manager.managers.minecraft.CombatManager
import org.progreso.client.module.Category
import org.progreso.client.module.Module

class Notifications : Module("Notifications", Category.Client) {
    private val modules by setting("Modules", true)
    private val pops by setting("Pops", true)

    init {
        safeEventListener<ModuleEvent> { event ->
            if (!modules) return@safeEventListener
            if (event.module is ClickGUI || event.module is Notifications) return@safeEventListener

            Api.CHAT.send("[Notifications] ${if (event.module.enabled) ChatFormatting.GREEN else ChatFormatting.RED}${event.module.name}")
        }

        safeEventListener<TotemPopEvent> { event ->
            if (!pops) return@safeEventListener
            if (event.player == mc.player) return@safeEventListener

            if (event.count != null) {
                Api.CHAT.send("[Notifications] ${event.player.name} has popped ${event.count} totems")
            }
        }

        safeEventListener<EntityDeathEvent> { event ->
            if (!pops) return@safeEventListener
            if (event.entity is EntityPlayer) {
                val pops = CombatManager[event.entity] ?: return@safeEventListener

                Api.CHAT.send("[Notifications] ${event.entity.name} died after popping $pops totems")
            }
        }
    }
}