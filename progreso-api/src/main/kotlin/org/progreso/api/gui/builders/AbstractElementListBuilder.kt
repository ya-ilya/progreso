package org.progreso.api.gui.builders

import org.progreso.api.common.MutableLazy.Companion.mutableLazy
import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.ElementListListeners
import org.progreso.api.gui.render.IRenderContext

abstract class AbstractElementListBuilder<ElementListWidget, Entry> : AbstractWidgetBuilder<ElementListWidget>() {
    protected val elementListListeners = ElementListListeners<ElementListWidget, Entry>()

    var headerHeight = 16

    var itemWidth by mutableLazy { width }
    var itemHeight = 32

    var top = 24
    var bottom by mutableLazy { height - 55 + 4 }
    var left = 0

    var renderSelection = true
    var renderHeader = false

    protected var children = mutableListOf<Entry>()

    fun listDimension(width: Int, height: Int, top: Int, bottom: Int, itemHeight: Int) {
        this.width = width
        this.height = height
        this.top = top
        this.bottom = bottom
        this.itemHeight = itemHeight
    }

    fun addEntry(entry: Entry) {
        children.add(entry)
    }

    fun renderHeader(block: ElementListWidget.(IRenderContext, Int, Int) -> Unit) {
        renderHeader = true
        elementListListeners.renderHeader = block
    }

    fun select(block: ElementListWidget.(Entry?) -> Unit) {
        elementListListeners.select = block
    }
}