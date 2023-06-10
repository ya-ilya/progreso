package org.progreso.api.gui

import org.progreso.api.gui.data.BuilderListeners

abstract class AbstractWidgetBuilder<Context, Widget> {
    protected val listeners = BuilderListeners<Context, Widget>()

    open var x: Int = 0
    open var y: Int = 0
    open var width: Int = 0
    open var height: Int = 0

    fun dimensions(x: Int, y: Int, width: Int, height: Int) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
    }

    fun init(block: Widget.() -> Unit) {
        listeners.init = block
    }

    fun render(block: Widget.(Context, Int, Int) -> Unit) {
        listeners.render = block
    }

    fun mouseClicked(block: Widget.(Int, Int, Int) -> Unit) {
        listeners.mouseClicked = block
    }

    fun mouseReleased(block: Widget.(Int, Int, Int) -> Unit) {
        listeners.mouseReleased = block
    }

    abstract fun build(): Widget
}