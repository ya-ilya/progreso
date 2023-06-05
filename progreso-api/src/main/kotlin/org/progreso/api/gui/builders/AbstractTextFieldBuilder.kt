package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.TextFieldListeners

abstract class AbstractTextFieldBuilder<TextFieldWidget> : AbstractWidgetBuilder<TextFieldWidget>() {
    protected val textFieldListeners = TextFieldListeners<TextFieldWidget>()

    var text = ""

    override var width = 200
    override var height = 20

    fun textChanged(block: TextFieldWidget.() -> Unit) {
        textFieldListeners.textChanged = block
    }
}