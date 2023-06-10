package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.components.ListComponent

class Window(
    override var x: Int,
    override var y: Int,
    override var width: Int
) : AbstractComponent() {
    private var dragging = false
    private var dragX = 0
    private var dragY = 0

    override var height = 0; get() = visibleComponents.sumOf { it.height }

    init {
        renderRect = false
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        if (dragging) {
            x = mouseX - dragX
            y = mouseY - dragY
        }

        super.render(context, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        val firstComponent = visibleComponents.firstOrNull()

        if (button == 0 && firstComponent is ListComponent
            && firstComponent.isHover(mouseX, mouseY)
            && firstComponent.header?.isHover(mouseX, mouseY) == true
        ) {
            dragging = true
            dragX = mouseX - x
            dragY = mouseY - y
        }

        super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false

        super.mouseReleased(mouseX, mouseY, state)
    }
}