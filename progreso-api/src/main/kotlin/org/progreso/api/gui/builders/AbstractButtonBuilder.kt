package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.ButtonListeners

abstract class AbstractButtonBuilder<Context, ButtonWidget>
    : AbstractWidgetBuilder<Context, ButtonWidget>() {
    protected val buttonListeners = ButtonListeners<ButtonWidget>()

    var text = ""
    var active = true

    override var width = 200
    override var height = 20

    fun onPress(block: ButtonWidget.() -> Unit) {
        buttonListeners.onPress = block
    }
}