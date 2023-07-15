package org.progreso.client.gui.clickgui.window.windows

import net.minecraft.client.gui.DrawContext
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.element.AbstractChildElement
import org.progreso.client.gui.clickgui.element.elements.ModuleElement
import org.progreso.client.gui.clickgui.window.AbstractWindow
import org.progreso.client.gui.invoke
import java.awt.Color

class CategoryWindow(category: Category, x: Int, y: Int, width: Int) : AbstractWindow(x, y, width) {
    init {
        windowElements.addAll(
            ModuleManager.getModulesByCategory(category).sortedBy { it.name }.map {
                ModuleElement(it, ClickGUI.ELEMENT_HEIGHT, this)
            }
        )

        header = object : AbstractChildElement(ClickGUI.ELEMENT_HEIGHT, this@CategoryWindow) {
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context {
                    drawCenteredString(
                        category.name,
                        Color.WHITE
                    )
                }
            }
        }
    }
}