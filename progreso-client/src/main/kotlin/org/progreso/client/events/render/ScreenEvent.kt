package org.progreso.client.events.render

import net.minecraft.client.gui.screen.Screen
import org.progreso.api.event.Event

sealed class ScreenEvent(val screen: Screen?) : Event() {
    class Set(screen: Screen?) : ScreenEvent(screen)
}