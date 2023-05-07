package org.progreso.client.gui

import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.components.ListComponent

@Suppress("SuspiciousVarProperty")
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

    override fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (dragging) {
            x = mouseX - dragX
            y = mouseY - dragY
        }

        super.drawComponent(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val firstComponent = visibleComponents.firstOrNull()

        if (mouseButton == 0 && firstComponent is ListComponent
            && firstComponent.isHover(mouseX, mouseY)
            && firstComponent.header?.isHover(mouseX, mouseY) == true
        ) {
            dragging = true
            dragX = mouseX - x
            dragY = mouseY - y
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false

        super.mouseReleased(mouseX, mouseY, state)
    }
}