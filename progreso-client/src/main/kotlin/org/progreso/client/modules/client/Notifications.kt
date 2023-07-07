package org.progreso.client.modules.client

import net.minecraft.util.Formatting
import org.progreso.api.Api
import org.progreso.api.event.events.ModuleEvent
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.entity.PlayerDeathEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.managers.CombatManager

@AbstractModule.Register("Notifications", Category.Client)
object Notifications : AbstractModule() {
    private val modules by setting("Modules", true)
    private val pops by setting("Pops", true)

    init {
        safeEventListener<ModuleEvent.Toggled> { event ->
            if (!modules) return@safeEventListener
            if (event.module is ClickGUI || event.module is HudEditor || event.module is Notifications) return@safeEventListener

            Api.CHAT.infoLocalized(
                "module.notifications.module_message",
                if (event.module.enabled) Formatting.GREEN else Formatting.RED,
                event.module.name
            )
        }

        safeEventListener<TotemPopEvent> { event ->
            if (!pops) return@safeEventListener
            if (event.player == mc.player) return@safeEventListener

            if (event.count != null) {
                Api.CHAT.infoLocalized(
                    "module.notifications.pop_message",
                    event.player.name.string,
                    event.count!!
                )
            }
        }

        safeEventListener<PlayerDeathEvent> { event ->
            if (!pops) return@safeEventListener

            Api.CHAT.infoLocalized(
                "module.notifications.death_message",
                event.player.name.string,
                (CombatManager[event.player] ?: return@safeEventListener)
            )
        }
    }
}