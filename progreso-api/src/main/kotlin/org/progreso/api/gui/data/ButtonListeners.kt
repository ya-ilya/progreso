package org.progreso.api.gui.data

data class ButtonListeners<Widget>(
    var onPress: Widget.() -> Unit = { }
)