package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.ElementListListeners

abstract class AbstractElementListBuilder<Context, ElementListWidget, Entry>
    : AbstractWidgetBuilder<Context, ElementListWidget>() {
    protected val elementListListeners = ElementListListeners<Context, ElementListWidget, Entry>()

    var itemWidth = 0
    var itemHeight = 32

    protected var children = mutableListOf<Entry>()
    protected var childrenInitializers = mutableListOf<ElementListWidget.() -> Entry>()

    fun listDimension(x: Int, y: Int, width: Int, height: Int, itemHeight: Int) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.itemHeight = itemHeight
        this.itemWidth = width
    }

    fun addEntry(entry: Entry) {
        children.add(entry)
    }

    fun addEntry(entry: ElementListWidget.() -> Entry) {
        childrenInitializers.add(entry)
    }

    fun select(block: ElementListWidget.(Entry?) -> Unit) {
        elementListListeners.select = block
    }
}