package org.progreso.api.gui.data

data class ElementListListeners<Context, Widget, Entry>(
    var select: Widget.(Entry?) -> Unit = { _ -> }
)