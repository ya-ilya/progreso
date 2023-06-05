package org.progreso.api.gui.data

data class ButtonListeners<ButtonWidget>(
    var onPress: ButtonWidget.() -> Unit = { }
)