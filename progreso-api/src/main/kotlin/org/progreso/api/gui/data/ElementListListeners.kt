package org.progreso.api.gui.data

data class ElementListListeners<Context, Widget, Entry>(
    var renderHeader: Widget.(Context, Int, Int) -> Unit = { _, _, _ -> },
    var select: Widget.(Entry?) -> Unit = { _ -> }
)