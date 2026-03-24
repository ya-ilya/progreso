package org.progreso.client.modules.movement

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener

@AbstractModule.AutoRegister
object AutoWalk : AbstractModule() {
    private val direction by setting("Direction", Direction.Forward).apply {
        valueChanged { oldValue, _ ->
            if (enabled) {
                oldValue.setPressed(false)
            }
        }
    }

    init {
        onDisable {
            for (direction in Direction.entries) {
                direction.setPressed(false)
            }
        }

        safeEventListener<TickEvent> {
            if (direction.isOppositePressed()) {
                direction.setPressed(false)
            } else {
                direction.setPressed(true)
            }
        }
    }

    private enum class Direction(
        val setPressed: (Boolean) -> Unit,
        val isOppositePressed: () -> Boolean
    ) {
        Forward({ mc.options.keyUp.isDown = it }, { mc.options.keyDown.isDown }),
        Backward({ mc.options.keyDown.isDown = it }, { mc.options.keyUp.isDown }),
        Left({ mc.options.keyLeft.isDown = it }, { mc.options.keyRight.isDown }),
        Right({ mc.options.keyRight.isDown = it }, { mc.options.keyLeft.isDown });
    }
}