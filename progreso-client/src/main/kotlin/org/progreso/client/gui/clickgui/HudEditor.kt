package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.input.MouseButtonEvent
import org.progreso.api.managers.ModuleManager
import org.progreso.api.module.AbstractHudModule
import org.progreso.api.module.Category
import org.progreso.client.gui.clickgui.window.windows.CategoryWindow
import org.progreso.client.gui.clickgui.window.windows.DescriptionWindow

object HudEditor : ClickGUI("HudEditor") {
    private val HUD_MODULES by lazy {
        ModuleManager.getModulesByCategory(Category.Hud)
            .filterIsInstance<AbstractHudModule>()
    }

    override fun initialize() {
        windows.add(CategoryWindow(Category.Hud, 10, 10, ELEMENT_WIDTH))
        windows.add(DescriptionWindow(10 + ELEMENT_WIDTH + X_INDENT, Y_INDENT, DESCRIPTION_WINDOW_WIDTH).also {
            descriptionWindow = it
        })
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractRenderState(graphics, mouseX, mouseY, delta)

        HUD_MODULES.filter { it.dragging }.forEach {
            it.x = mouseX - it.dragX
            it.y = mouseY - it.dragY
        }
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        val mouseXInt: Int = event.x.toInt()
        val mouseYInt: Int = event.y.toInt()

        HUD_MODULES.filter { it.enabled && it.isHover(mouseXInt, mouseYInt) }.forEach {
            it.dragging = true
            it.dragX = mouseXInt - it.x
            it.dragY = mouseYInt - it.y
        }

        return super.mouseClicked(event, doubleClick)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        HUD_MODULES.forEach { it.dragging = false }

        return super.mouseReleased(event)
    }
}