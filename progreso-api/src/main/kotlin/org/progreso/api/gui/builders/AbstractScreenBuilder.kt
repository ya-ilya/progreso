package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder

abstract class AbstractScreenBuilder<Context, Screen>
    : AbstractWidgetBuilder<Context, Screen>() {
    var title = ""
}