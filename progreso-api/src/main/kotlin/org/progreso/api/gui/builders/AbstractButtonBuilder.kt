package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.ButtonListeners

abstract class AbstractButtonBuilder<Context, Widget>
    : AbstractWidgetBuilder<Context, Widget>() {
    protected val buttonListeners = ButtonListeners<Widget>()

    var text = ""
    var active = true

    override var width = 200
    override var height = 20

    fun onPress(block: Widget.() -> Unit) {
        buttonListeners.onPress = block
    }
}