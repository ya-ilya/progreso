package org.progreso.api.module

/**
 * Hud module abstract class
 */
abstract class AbstractHudModule : AbstractModule() {
    var dragging = false
    var dragX = 0
    var dragY = 0

    var x by setting("X", 0, 0..Int.MAX_VALUE) { false }
    var y by setting("Y", 0, 0..Int.MAX_VALUE) { false }

    open var width: Int = 0
    open var height: Int = 0

    fun isHover(x: Int, y: Int) =
        x > this.x && x < this.x + width && y > this.y && y < this.y + height
}