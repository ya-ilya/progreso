package org.progreso.client.gui.clickgui.component.components

import net.minecraft.client.gui.DrawContext
import org.progreso.api.managers.ModuleManager
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.ChildComponent
import org.progreso.client.gui.use
import org.progreso.client.module.Category
import java.awt.Color

class CategoryComponent(
    category: Category,
    height: Int,
    parent: AbstractComponent
) : ListComponent(height, parent) {
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
            override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
                super.render(context, mouseX, mouseY)

                context.use {
                    drawCenteredString(
                        category.name,
                        Color.WHITE
                    )
                }
            }
        }
    }
}