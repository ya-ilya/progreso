package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder
import org.progreso.api.gui.data.ScreenListeners

abstract class AbstractScreenBuilder<Context, Screen>
    : AbstractWidgetBuilder<Context, Screen>() {
    protected val screenListeners = ScreenListeners<Screen>()

    var title = ""

    fun close(block: Screen.() -> Unit) {
        screenListeners.close = block
    }
}