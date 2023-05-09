package org.progreso.client.gui.component.components

import org.progreso.api.managers.ModuleManager
import org.progreso.client.gui.ClickGUI
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.ChildComponent
import org.progreso.client.module.Category
import org.progreso.client.util.Render2DUtil.drawCenteredString
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
            override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
                super.drawComponent(mouseX, mouseY, partialTicks)

                drawCenteredString(
                    category.name,
                    Color.WHITE
                )
            }
        }

        CATEGORY_COMPONENTS[category] = this
    }
}