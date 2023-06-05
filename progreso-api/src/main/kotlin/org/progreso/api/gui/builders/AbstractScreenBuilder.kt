package org.progreso.api.gui.builders

import org.progreso.api.gui.AbstractWidgetBuilder

abstract class AbstractScreenBuilder<Screen> : AbstractWidgetBuilder<Screen>() {
    var title = ""
}