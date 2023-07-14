package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.TextFieldListeners

abstract class AbstractTextFieldBuilder<Context, Widget>
    : AbstractWidgetBuilder<Context, Widget>() {
    protected val textFieldListeners = TextFieldListeners<Widget>()

    var text = ""

    override var width = 200
    override var height = 20

    fun textChanged(block: Widget.() -> Unit) {
        textFieldListeners.textChanged = block
    }
}