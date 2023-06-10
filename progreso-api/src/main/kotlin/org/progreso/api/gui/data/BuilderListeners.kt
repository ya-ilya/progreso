package org.progreso.api.gui.data

data class BuilderListeners<Context, Widget>(
    var init: Widget.() -> Unit = { },
    var render: Widget.(Context, Int, Int) -> Unit = { _, _, _ -> },
    var mouseClicked: Widget.(Int, Int, Int) -> Unit = { _, _, _ -> },
    var mouseReleased: Widget.(Int, Int, Int) -> Unit = { _, _, _ -> }
)