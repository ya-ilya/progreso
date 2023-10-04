package org.progreso.api.gui.data

data class ScreenListeners<Screen>(
    var close: Screen.() -> Unit = { }
)