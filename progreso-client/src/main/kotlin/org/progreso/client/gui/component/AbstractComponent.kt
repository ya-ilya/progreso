package org.progreso.client.gui.component

import org.progreso.client.gui.ClickGUI
import org.progreso.client.gui.component.data.ComponentOffsets
import org.progreso.client.util.Render2DUtil
import java.awt.Color

abstract class AbstractComponent {
    protected companion object {
        val theme: Color get() = ClickGUI.MODULE.theme
    }

    var components = mutableListOf<AbstractComponent>()

    open var x = 0
    open var y = 0
    open var width = 0
    open var height = 0
    open val offsets = ComponentOffsets(10)

    protected var renderRect = true
    protected open val visible = true

    protected val visibleComponents get() = components.filter { it.visible }

    open fun drawComponent(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (renderRect) {
            Render2DUtil.drawRect(x, y, width, height, ClickGUI.DEFAULT_RECT_COLOR)
        }

        visibleComponents.forEach { it.drawComponent(mouseX, mouseY, partialTicks) }
    }

    open fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        visibleComponents.forEach {
            if (it.isHover(mouseX, mouseY)) {
                it.mouseClicked(mouseX, mouseY, mouseButton)
            } else {
                it.mouseClickedOutside(mouseX, mouseY, mouseButton)
            }
        }
    }

    open fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        visibleComponents.forEach { it.mouseClickedOutside(mouseX, mouseY, mouseButton) }
    }

    open fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        visibleComponents.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    open fun keyTyped(typedChar: Char, keyCode: Int) {
        visibleComponents.forEach { it.keyTyped(typedChar, keyCode) }
    }

    fun isHover(x: Int, y: Int) =
        x > this.x && x < this.x + width && y > this.y && y < this.y + height

    fun getComponentY(component: AbstractComponent): Int {
        return try {
            y + visibleComponents.subList(0, visibleComponents.indexOf(component)).sumOf { it.height }
        } catch (ex: Exception) {
            0
        }
    }
}