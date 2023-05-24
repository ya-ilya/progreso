package org.progreso.client.gui.clickgui

import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.gui.clickgui.component.components.CategoryComponent
import org.progreso.client.module.Category

class HudEditor : ClickGUI() {
    private companion object {
        val HUD_MODULES by lazy {
            ModuleManager.getModulesByCategory(Category.Hud)
                .filterIsInstance<AbstractHudModule>()
        }
    }

    override fun initialize() {
        components.add(Window(10, 10, COMPONENT_WIDTH).apply {
            this.components.add(
                CategoryComponent(
                    Category.Hud,
                    COMPONENT_HEIGHT,
                    this
                )
            )
        })
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        HUD_MODULES.filter { it.dragging }.forEach {
            it.x = mouseX - it.dragX
            it.y = mouseY - it.dragY
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        HUD_MODULES.filter { it.isHover(mouseX, mouseY) }.forEach {
            it.dragging = true
            it.dragX = mouseX - it.x
            it.dragY = mouseY - it.y
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        HUD_MODULES.forEach { it.dragging = false }
    }
}