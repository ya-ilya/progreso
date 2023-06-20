package org.progreso.client.gui.clickgui.component

import net.minecraft.client.gui.DrawContext
import org.progreso.client.gui.clickgui.component.data.ComponentOffsets
import org.progreso.client.gui.invoke
import org.progreso.client.module.modules.client.ClickGUI

abstract class AbstractComponent {
    protected companion object {
        val theme get() = ClickGUI.theme
        val rectColor get() = ClickGUI.rectColor
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

    open fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        context {
            if (renderRect) {
                drawRect(x, y, width, height, rectColor)
            }
        }

        visibleComponents.forEach { it.render(context, mouseX, mouseY) }
    }

    open fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        visibleComponents.forEach {
            if (it.isHover(mouseX, mouseY)) {
                it.mouseClicked(mouseX, mouseY, button)
            } else {
                it.mouseClickedOutside(mouseX, mouseY, button)
            }
        }
    }

    open fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        visibleComponents.forEach { it.mouseClickedOutside(mouseX, mouseY, mouseButton) }
    }

    open fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        visibleComponents.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    open fun keyPressed(keyCode: Int, scanCode: Int) {
        visibleComponents.forEach { it.keyPressed(keyCode, scanCode) }
    }

    open fun charTyped(char: Char) {
        visibleComponents.forEach { it.charTyped(char) }
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