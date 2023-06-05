package org.progreso.api.gui.data

import org.progreso.api.gui.render.IRenderContext

data class BuilderListeners<Widget>(
    var init: Widget.() -> Unit = { },
    var render: Widget.(IRenderContext, Int, Int) -> Unit = { _, _, _ -> },
    var mouseClicked: Widget.(Int, Int, Int) -> Unit = { _, _, _ -> },
    var mouseReleased: Widget.(Int, Int, Int) -> Unit = { _, _, _ -> }
)