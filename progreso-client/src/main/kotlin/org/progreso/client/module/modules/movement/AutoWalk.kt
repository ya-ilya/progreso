package org.progreso.client.module.modules.movement

import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener
import org.progreso.client.module.Category
import org.progreso.client.module.Module

object AutoWalk : Module("AutoWalk", Category.Movement) {
    private val direction by setting("Direction", Direction.Forward)

    init {
        onDisable {
            for (direction in Direction.values()) {
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
        Forward({ mc.options.forwardKey.isPressed = it }, { mc.options.backKey.isPressed }),
        Backward({ mc.options.backKey.isPressed = it }, { mc.options.forwardKey.isPressed }),
        Left({ mc.options.leftKey.isPressed = it }, { mc.options.rightKey.isPressed }),
        Right({ mc.options.rightKey.isPressed = it }, { mc.options.leftKey.isPressed });
    }
}