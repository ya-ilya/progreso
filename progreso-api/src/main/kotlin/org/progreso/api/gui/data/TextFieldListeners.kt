package org.progreso.api.gui.data

data class TextFieldListeners<Widget>(
    var textChanged: Widget.() -> Unit = { }
)