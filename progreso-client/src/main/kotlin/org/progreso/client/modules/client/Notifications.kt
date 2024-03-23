package org.progreso.client.modules.client

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Formatting
import org.progreso.api.Api
import org.progreso.api.event.events.ModuleEvent
import org.progreso.api.managers.FriendManager
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.entity.EntityDeathEvent
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.player.TotemPopEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.managers.CombatManager
import org.progreso.client.util.client.TimeUnit
import org.progreso.client.util.client.createTimer

@AbstractModule.AutoRegister
object Notifications : AbstractModule() {
    private val modules by setting("Modules", true)
    private val pops by setting("Pops", true)
    private val visualRange by setting("VisualRange", false)

    private val visualRangeTimer = createTimer(TimeUnit.Second)
    private val visualRangePlayers = mutableSetOf<PlayerEntity>()

    init {
        safeEventListener<ModuleEvent.Toggle> { event ->
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

        safeEventListener<EntityDeathEvent> { event ->
            if (!pops || event.entity !is PlayerEntity) return@safeEventListener

            Api.CHAT.infoLocalized(
                "module.notifications.death_message",
                event.entity.name.string,
                CombatManager[event.entity] ?: return@safeEventListener
            )
        }

        safeEventListener<TickEvent> { _ ->
            if (!visualRange || !visualRangeTimer.tick(1L)) return@safeEventListener

            val players = mc.world.players.toHashSet()

            for (player in players) {
                if (player == mc.player || FriendManager.isFriend(player.name.string)) continue

                if (visualRangePlayers.add(player)) {
                    Api.CHAT.infoLocalized(
                        "module.notifications.visualrange_enter_message",
                        player.name.string
                    )
                }
            }

            for (player in visualRangePlayers.toHashSet()) {
                if (!players.contains(player)) {
                    visualRangePlayers.remove(player)

                    Api.CHAT.infoLocalized(
                        "module.notifications.visualrange_leave_message",
                        player.name.string
                    )
                }
            }
        }

        onDisable {
            visualRangeTimer.reset()
            visualRangePlayers.clear()
        }
    }
}