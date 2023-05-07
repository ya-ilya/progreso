package org.progreso.client.gui

import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Mouse
import org.progreso.api.managers.ModuleManager
import org.progreso.client.gui.component.AbstractComponent
import org.progreso.client.gui.component.components.CategoryComponent
import org.progreso.client.module.Category
import org.progreso.client.module.modules.client.ClickGUI
import java.awt.Color

open class ClickGUI : GuiScreen() {
    companion object {
        const val COMPONENT_HEIGHT = 14
        const val COMPONENT_WIDTH = 90

        val DEFAULT_RECT_COLOR = Color(0, 0, 0, 130)
        val MODULE by lazy { ModuleManager[ClickGUI::class] }
    }

    private var components = mutableListOf<AbstractComponent>()

    fun initialize() {
        var x = 10

        for (category in Category.values()) {
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
            components.forEach { it.y += MODULE.scrollSpeed }
        } else if (wheel < 0) {
            components.forEach { it.y -= MODULE.scrollSpeed }
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