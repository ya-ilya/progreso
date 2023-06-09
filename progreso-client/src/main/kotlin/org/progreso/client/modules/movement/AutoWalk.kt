package org.progreso.client.modules.movement

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import org.progreso.client.events.misc.TickEvent
import org.progreso.client.events.safeEventListener

@AbstractModule.Register("AutoWalk", Category.Movement)
object AutoWalk : AbstractModule() {
    private val direction by setting("Direction", Direction.Forward).apply {
        valueChanged { oldValue, _ ->
            if (mc.options != null) {
                oldValue.setPressed(false)
            }
        }
    }

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
        Forward({ mc.options!!.forwardKey.isPressed = it }, { mc.options!!.backKey.isPressed }),
        Backward({ mc.options!!.backKey.isPressed = it }, { mc.options!!.forwardKey.isPressed }),
        Left({ mc.options!!.leftKey.isPressed = it }, { mc.options!!.rightKey.isPressed }),
        Right({ mc.options!!.rightKey.isPressed = it }, { mc.options!!.leftKey.isPressed });
    }
}