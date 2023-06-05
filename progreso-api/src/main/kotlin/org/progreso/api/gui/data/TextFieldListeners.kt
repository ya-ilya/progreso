package org.progreso.api.gui.data

data class TextFieldListeners<TextFieldWidget>(
    var textChanged: TextFieldWidget.() -> Unit = { }
)