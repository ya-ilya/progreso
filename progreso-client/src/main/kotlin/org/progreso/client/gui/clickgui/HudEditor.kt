package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.DrawContext
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractHudModule
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.window.windows.CategoryWindow

object HudEditor : ClickGUI("HudEditor") {
    private val HUD_MODULES by lazy {
        ModuleManager.getModulesByCategory(Category.Hud)
            .filterIsInstance<AbstractHudModule>()
    }

    override fun initialize() {
        windows.add(CategoryWindow(Category.Hud, 10, 10, ELEMENT_WIDTH))
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        HUD_MODULES.filter { it.dragging }.forEach {
            it.x = mouseX - it.dragX
            it.y = mouseY - it.dragY
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val mouseXInt: Int = mouseX.toInt()
        val mouseYInt: Int = mouseY.toInt()

        HUD_MODULES.filter { it.isHover(mouseXInt, mouseYInt) }.forEach {
            it.dragging = true
            it.dragX = mouseXInt - it.x
            it.dragY = mouseYInt - it.y
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        HUD_MODULES.forEach { it.dragging = false }

        return super.mouseReleased(mouseX, mouseY, button)
    }
}