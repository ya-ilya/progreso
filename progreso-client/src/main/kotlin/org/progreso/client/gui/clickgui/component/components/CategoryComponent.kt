package org.progreso.client.gui.clickgui.component.components

import org.progreso.api.managers.ModuleManager
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.module.Category
import org.progreso.client.util.render.RenderContext
import java.awt.Color

class CategoryComponent(
    category: Category,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
    companion object {
        val CATEGORY_COMPONENTS = mutableMapOf<Category, CategoryComponent>()
    }

    init {
        listComponents.addAll(
            ModuleManager.getModulesByCategory(category).sortedBy { it.name }.map {
                ModuleComponent(
                    it,
                    ClickGUI.COMPONENT_HEIGHT,
                    this
                )
            }
        )

        header = object : ChildComponent(height, this@CategoryComponent) {
            override fun render(context: RenderContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context {
                    drawCenteredString(
                        category.name,
                        Color.WHITE
                    )
                }
            }
        }

        CATEGORY_COMPONENTS[category] = this
    }
}