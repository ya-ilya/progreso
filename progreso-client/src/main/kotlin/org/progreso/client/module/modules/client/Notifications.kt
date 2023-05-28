package org.progreso.client.module.modules.client

import net.minecraft.util.Formatting
import org.progreso.api.Api
import org.progreso.api.event.events.ModuleEvent
import org.progreso.client.events.entity.PlayerDeathEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.manager.managers.minecraft.CombatManager
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object Notifications : Module("Notifications", Category.Client) {
    private val modules by setting("Modules", true)
    private val pops by setting("Pops", true)

    init {
        safeEventListener<ModuleEvent> { event ->
            if (!modules) return@safeEventListener
            if (event.module is ClickGUI || event.module is HudEditor || event.module is Notifications) return@safeEventListener

            Api.CHAT.info("[Notifications] ${if (event.module.enabled) Formatting.GREEN else Formatting.RED}${event.module.name}")
        }

        safeEventListener<TotemPopEvent> { event ->
            if (!pops) return@safeEventListener
            if (event.player == mc.player) return@safeEventListener

            if (event.count != null) {
                Api.CHAT.info("[Notifications] ${event.player.name} has popped ${event.count} totems")
            }
        }

        safeEventListener<PlayerDeathEvent> { event ->
            if (!pops) return@safeEventListener
            val pops = CombatManager[event.player] ?: return@safeEventListener

            Api.CHAT.info("[Notifications] ${event.player.name} died after popping $pops totems")
        }
    }
}