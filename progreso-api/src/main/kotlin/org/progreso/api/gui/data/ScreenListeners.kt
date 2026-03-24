package org.progreso.api.gui.data

data class ScreenListeners<Screen>(
    var onClose: Screen.() -> Unit = { }
)