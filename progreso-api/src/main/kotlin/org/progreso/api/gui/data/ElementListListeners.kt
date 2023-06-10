package org.progreso.api.gui.data

data class ElementListListeners<Context, ElementListWidget, Entry>(
    var renderHeader: ElementListWidget.(Context, Int, Int) -> Unit = { _, _, _ -> },
    var select: ElementListWidget.(Entry?) -> Unit = { _ -> }
)