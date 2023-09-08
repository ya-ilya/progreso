package org.progreso.client.gui.clickgui.element

import net.minecraft.client.gui.DrawContext

interface ParentElement : Element {
    val elements: MutableList<AbstractChildElement>
    val visibleElements get() = elements.filter { it.visible }

    // override var height = 0; get() = visibleElements.sumOf { it.height }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int) {
        visibleElements.forEach { it.render(context, mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        visibleElements.forEach {
            if (it.isHover(mouseX, mouseY)) {
                it.mouseClicked(mouseX, mouseY, button)
            } else {
                it.mouseClickedOutside(mouseX, mouseY, button)
            }
        }
    }

    override fun mouseClickedOutside(mouseX: Int, mouseY: Int, mouseButton: Int) {
        visibleElements.forEach { it.mouseClickedOutside(mouseX, mouseY, mouseButton) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        visibleElements.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int) {
        visibleElements.forEach { it.keyPressed(keyCode, scanCode) }
    }

    override fun charTyped(char: Char) {
        visibleElements.forEach { it.charTyped(char) }
    }

    fun getChildElementY(element: Element): Int {
        return try {
            y + visibleElements.subList(0, visibleElements.indexOf(element)).sumOf { it.height }
        } catch (ex: Exception) {
            0
        }
    }
}