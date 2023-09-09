package org.progreso.client.gui.clickgui.window.windows

import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.element.elements.ModuleElement
import org.progreso.client.gui.clickgui.window.AbstractWindow

class CategoryWindow(category: Category, x: Int, y: Int, width: Int) : AbstractWindow(x, y, width) {
    init {
        windowElements.addAll(
            ModuleManager.getModulesByCategory(category).sortedBy { it.name }.map {
                ModuleElement(it, ClickGUI.ELEMENT_HEIGHT, this)
            }
        )

        header = HeaderElement(category.name, this)
    }
}