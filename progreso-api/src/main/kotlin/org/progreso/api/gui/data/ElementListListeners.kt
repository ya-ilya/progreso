package org.progreso.api.gui.data

import org.progreso.api.gui.render.IRenderContext

data class ElementListListeners<ElementListWidget, Entry>(
    var renderHeader: ElementListWidget.(IRenderContext, Int, Int) -> Unit = { _, _, _ -> },
    var select: ElementListWidget.(Entry?) -> Unit = { _ -> }
)