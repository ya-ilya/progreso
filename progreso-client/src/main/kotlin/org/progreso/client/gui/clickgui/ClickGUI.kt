package org.progreso.client.gui.clickgui

import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import org.progreso.client.gui.clickgui.component.AbstractComponent
import org.progreso.client.gui.clickgui.component.components.CategoryComponent
import org.progreso.client.module.Category
import org.progreso.client.module.modules.client.ClickGUI

open class ClickGUI : GuiScreen() {
    companion object : org.progreso.client.gui.clickgui.ClickGUI() {
        const val COMPONENT_HEIGHT = 14
        const val COMPONENT_WIDTH = 90
    }

    protected var components = mutableListOf<AbstractComponent>()

    open fun initialize() {
        var x = 10

        for (category in Category.values().filter { it != Category.Hud }) {
            components.add(Window(x, 10, COMPONENT_WIDTH).apply {
                x += COMPONENT_WIDTH + 10

                this.components.add(
                    CategoryComponent(
                        category,
                        COMPONENT_HEIGHT,
                        this
                    )
                )
            })
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)

        val wheel = Mouse.getDWheel()

        if (wheel > 0) {
            components.forEach { it.y += ClickGUI.scrollSpeed }
        } else if (wheel < 0) {
            components.forEach { it.y -= ClickGUI.scrollSpeed }
        }

        components.forEach { it.drawComponent(mouseX, mouseY, partialTicks) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)

        val window = components.lastOrNull { it.isHover(mouseX, mouseY) }

        if (window != null) {
            components.remove(window)
            components.add(window)

            window.mouseClicked(mouseX, mouseY, mouseButton)
        }

        components.filter { it != window }.forEach { it.mouseClickedOutside(mouseX, mouseY, mouseButton) }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)

        components.forEach { it.mouseReleased(mouseX, mouseY, state) }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        super.keyTyped(typedChar, keyCode)

        components.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun doesGuiPauseGame(): Boolean = false
}